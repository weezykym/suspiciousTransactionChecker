package com.interview.FraudDetectionSystem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@RequiredArgsConstructor
@Table(name = "fee_transactions")
public class FeeTransaction {

    //getters and setters
    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Getter
    private String studentId;
    private Double amount;
    private String paymentMethod;
    private LocalDateTime transactionTime;
    private Double initialBalance;
    private Boolean isSuspicious;


}
