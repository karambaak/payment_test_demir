package com.example.payment.repositories;

import com.example.payment.entities.Token;
import com.example.payment.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokensRepository extends JpaRepository<Token, Long> {

    boolean existsByUser_Login(String login);

    Token findByUser_Login(String login);

    @Transactional
    void deleteByUser_Login(String login);
}
