package com.example.payment.errors.exceptions;

public class NotEnoughBalanceException extends Exception {
    public NotEnoughBalanceException() {
        super("Transaction canceled. Balance is not enough!");
    }
}
