package com.example.payment.services.impl;

import com.example.payment.dtos.JwtAuthenticationResponse;
import com.example.payment.dtos.UserDto;
import com.example.payment.entities.User;
import com.example.payment.errors.InvalidLoginException;
import com.example.payment.repositories.AuthorityRepository;
import com.example.payment.repositories.UserRepository;
import com.example.payment.services.AuthenticationService;
import com.example.payment.services.JwtService;
import com.example.payment.services.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final AuthorityRepository authorityRepository;
    private final TokenService tokenService;

    @Override
    public JwtAuthenticationResponse login(UserDto userDto, HttpServletRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDto.getLogin(), userDto.getPassword()));
        User user = userRepository.findById(userDto.getLogin())
                .orElseThrow(() -> new IllegalArgumentException("Invalid login or password"));
        if (tokenService.isTokenExistByUser(user.getLogin())) {
            String existToken = tokenService.getTokenByUserLogin(user.getLogin());
            if (!jwtService.isTokenExpired(existToken)) {
                return JwtAuthenticationResponse.builder()
                        .token(existToken)
                        .build();
            }
        }
        String newToken = jwtService.generateToken(user);
        tokenService.addUserToken(user, newToken);
        return JwtAuthenticationResponse.builder()
                .token(newToken)
                .build();

    }

    @Override
    public JwtAuthenticationResponse registry(UserDto userDto) throws InvalidLoginException {
        Optional<User> user = userRepository.findById(userDto.getLogin());
        if (user.isPresent()) {
            throw new InvalidLoginException("User with login: " + userDto.getLogin() + " is already exist");
        }
        User newUser = User.builder()
                .login(userDto.getLogin())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .enabled(true)
                .authority(authorityRepository.findByAuthorityNameIgnoreCase("user")
                        .orElseThrow(() -> new NoSuchElementException("Authority not found")))
                .build();
        userRepository.save(newUser);
        String token = jwtService.generateToken(newUser);
        tokenService.addUserToken(newUser, token);
        return JwtAuthenticationResponse.builder()
                .token(token)
                .build();
    }

    @Override
    public void logout(HttpServletRequest request) {
        String login = request.getUserPrincipal().getName();
        tokenService.deleteTokenByUserLogin(login);
    }
}
