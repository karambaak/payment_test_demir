package com.example.payment.configs.bruteForceSecurity;

import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationFailureListener.class);
    @Resource(name = "bruteForceProtectionService")
    private DefaultBruteForceProtectionService bruteForceProtectionService;

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        String username = event.getAuthentication().getName();
        LOG.info("Login failed for user {} ", username);
        bruteForceProtectionService.registerLoginFailure(username);
    }
}
