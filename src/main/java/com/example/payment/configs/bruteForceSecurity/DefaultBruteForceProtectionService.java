package com.example.payment.configs.bruteForceSecurity;

import com.example.payment.entities.User;
import com.example.payment.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("bruteForceProtectionService")
@RequiredArgsConstructor
@Slf4j
public class DefaultBruteForceProtectionService implements BruteForceProtectionService {

    @Value("${jdj.security.failedLogin.count}")
    private int maxFailedLogins;

    private final UserRepository userRepository;

    @Override
    public void registerLoginFailure(String username) {
        User user = getUser(username);
        if (user != null && !user.isLoginDisabled()) {
            int failedCounter = user.getFailedLoginAttempts();
            log.info("Login failed {}, fails = {}", username, failedCounter);
            if (maxFailedLogins < failedCounter + 1) {
                user.setLoginDisabled(true);
                log.info("{} user is blocked", username);
            } else {
                user.setFailedLoginAttempts(failedCounter + 1);
            }
            userRepository.save(user);
        }
    }

    @Override
    public void resetBruteForceCounter(String username) {
        User user = getUser(username);
        if (user != null) {
            user.setFailedLoginAttempts(0);
            user.setLoginDisabled(false);
            userRepository.save(user);
        }
    }

    @Override
    public boolean isBruteForceAttack(String username) {
        User user = getUser(username);
        if (user != null) {
            return user.getFailedLoginAttempts() >= maxFailedLogins;
        }
        return false;
    }

    private User getUser(final String login) {
        return userRepository.findById(login)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
