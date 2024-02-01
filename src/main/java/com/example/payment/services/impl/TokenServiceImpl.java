package com.example.payment.services.impl;

import com.example.payment.entities.Token;
import com.example.payment.entities.User;
import com.example.payment.repositories.TokensRepository;
import com.example.payment.services.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final TokensRepository tokensRepository;
    @Override
    public void addUserToken(User user, String token) {
        tokensRepository.saveAndFlush(Token.builder()
                        .user(user)
                        .token(token)
                .build());
    }

    @Override
    public boolean isTokenExistByUser(String login) {
        return tokensRepository.existsByUser_Login(login);
    }

    @Override
    public String getTokenByUserLogin(String login) {
        return tokensRepository.findByUser_Login(login).getToken();
    }

    @Override
    public void deleteTokenByUserLogin(String login) {
        tokensRepository.deleteByUser_Login(login);
    }
}
