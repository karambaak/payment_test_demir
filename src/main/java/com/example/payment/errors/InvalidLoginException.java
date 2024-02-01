package com.example.payment.errors;

public class InvalidLoginException extends Exception{
    public InvalidLoginException(String message) {
        super(message);
    }
}
