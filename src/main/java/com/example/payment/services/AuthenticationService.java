package com.example.payment.services;

import com.example.payment.dtos.UserDto;
import com.example.payment.dtos.JwtAuthenticationResponse;
import com.example.payment.errors.exceptions.InvalidLoginException;
import com.example.payment.errors.exceptions.LoginDisabledException;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthenticationService {

    JwtAuthenticationResponse login(UserDto userDto, HttpServletRequest request) throws LoginDisabledException;

    JwtAuthenticationResponse registry(UserDto userDto) throws InvalidLoginException;

    void logout(HttpServletRequest request);
}
