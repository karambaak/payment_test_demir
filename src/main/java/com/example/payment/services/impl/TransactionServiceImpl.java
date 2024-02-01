package com.example.payment.services.impl;

import com.example.payment.entities.Transaction;
import com.example.payment.entities.User;
import com.example.payment.errors.NotEnoughBalanceException;
import com.example.payment.repositories.TransactionsRepository;
import com.example.payment.repositories.UserRepository;
import com.example.payment.services.JwtService;
import com.example.payment.services.TransactionsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionsService {

    private final TransactionsRepository transactionsRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    public void createTransaction(HttpServletRequest request, String transTo, double amount) throws NotEnoughBalanceException {
        String token = request.getHeader("Authorization").substring(7);
        String transFrom = jwtService.extractUserName(token);
        if (balanceCheck(transFrom, amount)){
            setNewUsersBalance(transFrom,amount);
            transactionsRepository.saveAndFlush(Transaction.builder()
                    .transactionFrom(transFrom)
                    .transactionTo(transTo)
                    .dateTime(LocalDateTime.now())
                    .amount(amount)
                    .build());
        } else {
            throw new NotEnoughBalanceException();
        }
    }

    private boolean balanceCheck(String login, double amount) {
        User user = userRepository.findById(login).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return user.getBalance() > amount;
    }

    private void setNewUsersBalance(String login, double amount){
        User user = userRepository.findById(login)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setBalance(user.getBalance() - amount);
        userRepository.save(user);
    }
}
