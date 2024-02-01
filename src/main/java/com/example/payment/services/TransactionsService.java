package com.example.payment.services;

import com.example.payment.errors.NotEnoughBalanceException;
import jakarta.servlet.http.HttpServletRequest;

public interface TransactionsService {

    void createTransaction(HttpServletRequest request, String transTo, double amount) throws NotEnoughBalanceException;
}
