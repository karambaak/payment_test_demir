package com.example.payment.controlers;

import com.example.payment.errors.exceptions.NotEnoughBalanceException;
import com.example.payment.services.TransactionsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TransactionController {

    private final TransactionsService transactionsService;
    @PostMapping("/payment")
    public ResponseEntity<String> doTransaction(@NonNull HttpServletRequest request) throws NotEnoughBalanceException {
        transactionsService.createTransaction(request, "some other user", 1.1D);
        return new ResponseEntity<>("Transaction completely finished", HttpStatus.OK);
    }
}
