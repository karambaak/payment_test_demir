package com.example.payment.errors.handler;

import com.example.payment.errors.errorService.ErrorService;
import com.example.payment.errors.exceptions.InvalidLoginException;
import com.example.payment.errors.exceptions.LoginDisabledException;
import com.example.payment.errors.exceptions.NotEnoughBalanceException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionsHandler {
    private final ErrorService errorService;

    @ExceptionHandler(NoSuchElementException.class)
    private ResponseEntity<?> noSuchElementHandler(NoSuchElementException exception) {
        return new ResponseEntity<>(errorService.makeBody(exception), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidLoginException.class)
    private ResponseEntity<?> invalidCredentialsHandler(InvalidLoginException exception) {
        return new ResponseEntity<>(errorService.makeBody(exception), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NotEnoughBalanceException.class)
    private ResponseEntity<?> notEnoughBalanceHandler(NotEnoughBalanceException exception) {
        return new ResponseEntity<>(errorService.makeBody(exception), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    private ResponseEntity<?> illegalArgumentHandler(IllegalArgumentException exception) {
        return new ResponseEntity<>(errorService.makeBody(exception), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    private ResponseEntity<?> usernameNotFoundHandler(UsernameNotFoundException exception) {
        return new ResponseEntity<>(errorService.makeBody(exception), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LoginDisabledException.class)
    private ResponseEntity<?> usernameNotFoundHandler(LoginDisabledException exception) {
        return new ResponseEntity<>(errorService.makeBody(exception), HttpStatus.LOCKED);
    }
}
