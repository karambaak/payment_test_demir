package com.example.payment.services;

import com.example.payment.entities.User;

public interface TokenService {

    void addUserToken(User user, String token);

    boolean isTokenExist(User user);

    String getTokenByUserLogin(String login);
}
