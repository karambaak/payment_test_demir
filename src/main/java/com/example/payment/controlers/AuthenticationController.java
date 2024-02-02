package com.example.payment.controlers;

import com.example.payment.dtos.JwtAuthenticationResponse;
import com.example.payment.dtos.UserDto;
import com.example.payment.errors.exceptions.InvalidLoginException;
import com.example.payment.errors.exceptions.LoginDisabledException;
import com.example.payment.services.impl.AuthenticationServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationServiceImpl authenticationServiceImpl;
    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> login(@RequestBody UserDto userDto, @NonNull HttpServletRequest request)
            throws LoginDisabledException
    {
        return ResponseEntity.ok(authenticationServiceImpl.login(userDto, request));
    }

    @PostMapping("/registry")
    public ResponseEntity<JwtAuthenticationResponse> registry(@RequestBody UserDto userDto) throws InvalidLoginException {
        return ResponseEntity.ok(authenticationServiceImpl.registry(userDto));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request){
        authenticationServiceImpl.logout(request);
        return ResponseEntity.ok("Completely logout");
    }
}
