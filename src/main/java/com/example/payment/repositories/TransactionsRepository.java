package com.example.payment.repositories;

import com.example.payment.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionsRepository extends JpaRepository<Transaction, Long> {
}
