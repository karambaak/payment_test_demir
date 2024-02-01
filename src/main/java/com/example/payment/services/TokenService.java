package com.example.payment.services;

import com.example.payment.entities.User;

public interface TokenService {

    void addUserToken(User user, String token);

    public boolean isTokenExistByUser(String login);

    String getTokenByUserLogin(String login);

    void deleteTokenByUserLogin(String login);
}
