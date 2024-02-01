package com.example.payment.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "transactions")
public class Transaction {

    @Id
    private Long id;

    private LocalDateTime dateTime;

    private String transactionFrom;

    private String transactionTo;

    private Double amount;
}
