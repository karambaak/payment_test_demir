package com.example.payment.services;

import com.example.payment.dtos.JwtAuthenticationResponse;
import com.example.payment.dtos.UserDto;
import com.example.payment.entities.Authority;
import com.example.payment.entities.User;
import com.example.payment.errors.exceptions.InvalidLoginException;
import com.example.payment.errors.exceptions.LoginDisabledException;
import com.example.payment.repositories.AuthorityRepository;
import com.example.payment.repositories.UserRepository;
import com.example.payment.services.impl.AuthenticationServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.Principal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationServiceImpl authenticationServiceImpl;

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private AuthorityRepository authorityRepository;
    @Mock
    private TokenService tokenService;
    @Mock
    private User user;
    @Mock
    private HttpServletRequest request;

    @Test
    void login_ValidUser_ReturnsJwtAuthenticationResponse() throws LoginDisabledException {
        UserDto userDto = new UserDto("user", "qwe");
        when(userRepository.findById(userDto.getLogin())).thenReturn(Optional.of(user));
        when(tokenService.isTokenExistByUser(user.getLogin())).thenReturn(false);
        when(jwtService.generateToken(user)).thenReturn("token");

        JwtAuthenticationResponse response = authenticationServiceImpl.login(userDto, mock(HttpServletRequest.class));

        assertNotNull(response);
        assertEquals("token", response.getToken());
        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken(userDto.getLogin(), userDto.getPassword()));
        verify(tokenService).addUserToken(user, "token");
    }

    @Test
    void login_ValidUser_withExistToken_ReturnsTokenFromDatabase() throws LoginDisabledException {
        UserDto userDto = new UserDto("user", "qwe");
        when(userRepository.findById(userDto.getLogin())).thenReturn(Optional.of(user));
        when(tokenService.isTokenExistByUser(user.getLogin())).thenReturn(true);
        when(tokenService.getTokenByUserLogin(user.getLogin())).thenReturn("existToken");

        JwtAuthenticationResponse response = authenticationServiceImpl.login(userDto, mock(HttpServletRequest.class));

        assertNotNull(response);
        assertEquals("existToken", response.getToken());
    }

    @Test
    void login_BruteForcedUser_throws_LoginDisabledException() {
        UserDto userDto = new UserDto("user", "12365");
        when(userRepository.findById(userDto.getLogin())).thenReturn(Optional.of(user));
        when(user.isLoginDisabled()).thenReturn(true);

        assertThrows(LoginDisabledException.class, () -> authenticationServiceImpl.login(userDto, mock(HttpServletRequest.class)));

        verify(authenticationManager, never()).authenticate(any());
        verify(tokenService, never()).addUserToken(any(), any());
    }

    @Test
    void registry_UserWithExistLogin_throws_InvalidLoginException() {
        UserDto userDto = new UserDto("user", "12365");
        Optional<User> user = Optional.of(new User());

        when(userRepository.findById(userDto.getLogin())).thenReturn(user);

        assertThrows(InvalidLoginException.class, () -> authenticationServiceImpl.registry(userDto));

        verify(userRepository, never()).save(any());
        verify(tokenService, never()).addUserToken(any(), any());

    }

    @Test
    void registry_UserWithValidLogin_ReturnsJwtAuthenticationResponse() throws InvalidLoginException {
        UserDto userDto = new UserDto("user", "12365");
        Authority authority = new Authority();

        when(userRepository.findById(userDto.getLogin())).thenReturn(Optional.empty());
        when(authorityRepository.findByAuthorityNameIgnoreCase("user")).thenReturn(Optional.of(authority));
        when(jwtService.generateToken(any(User.class))).thenReturn("token");
        JwtAuthenticationResponse response = authenticationServiceImpl.registry(userDto);

        assertNotNull(response);
        assertEquals("token", response.getToken());

        verify(userRepository, atLeastOnce()).save(any());
        verify(tokenService, atLeastOnce()).addUserToken(any(), any());
    }

    @Test
    void logoutCurrentUser_deleteToken(){
        Authentication authentication = new UsernamePasswordAuthenticationToken("someUser", "someUserPassword");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Principal principal = mock(Principal.class);
        when(request.getUserPrincipal()).thenReturn(principal);
        when(principal.getName()).thenReturn("someUser");

        authenticationServiceImpl.logout(request);

        verify(tokenService).deleteTokenByUserLogin("someUser");
    }

    @Test
    void logout_NoCurrentUser_DoesNotDeleteToken() {
        SecurityContextHolder.getContext().setAuthentication(null);

        assertThrows(UsernameNotFoundException.class, () -> authenticationServiceImpl.logout(request));
        verify(tokenService, never()).deleteTokenByUserLogin(any());

    }

}
