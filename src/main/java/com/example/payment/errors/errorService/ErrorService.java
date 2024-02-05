package com.example.payment.errors.errorService;

import com.example.payment.errors.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Slf4j
public class ErrorService {

    public ErrorResponse makeBody(Exception exception) {
        ErrorResponse error = ErrorResponse.builder()
                .title(exception.getMessage())
                .reasons(List.of(exception.getMessage()))
                .build();
        printErrorLog(error.getReasons().toString());
        return error;
    }

    private void printErrorLog(String message) {
        log.error("Exception message: {}", message);
    }
}

