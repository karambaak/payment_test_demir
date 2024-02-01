package com.example.payment.services;

import com.example.payment.dtos.UserDto;
import com.example.payment.dtos.JwtAuthenticationResponse;
import com.example.payment.errors.InvalidLoginException;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthenticationService {

    JwtAuthenticationResponse login(UserDto userDto, HttpServletRequest request);

    JwtAuthenticationResponse registry(UserDto userDto) throws InvalidLoginException;
}
