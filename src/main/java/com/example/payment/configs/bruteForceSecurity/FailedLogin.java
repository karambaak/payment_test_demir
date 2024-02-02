package com.example.payment.configs.bruteForceSecurity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class FailedLogin {

    private int count;

    private LocalDateTime time;

    public FailedLogin() {
        this.count = 0;
        this.time = LocalDateTime.now();
    }

    public FailedLogin(int count, LocalDateTime time) {
        this.count = count;
        this.time = time;
    }

}
