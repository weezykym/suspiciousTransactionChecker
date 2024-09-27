package com.interview.FraudDetectionSystem.repository;

import com.interview.FraudDetectionSystem.entity.FeeTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FeeTransactionRepository extends JpaRepository<FeeTransaction, Long> {
    /**
     *  First method is used to return a list of transaction objects using studentId and transactionTime as parameters
     * This method is for detecting unusually high payment frequencies made by hte same student
     */


    List<FeeTransaction> findByStudentIdAndTransactionTimeBetween(String studentId, LocalDateTime startTime, LocalDateTime endTime);

    /**second method is for detecting duplicate transactions i.e. same payment method, same amount within a 10-minute window */
    List<FeeTransaction> findByAmountAndPaymentMethodAndTransactionTimeBetween(Double amount, String paymentMethod, LocalDateTime startTime, LocalDateTime endTime);

    /** Third method is for payment clustering where payments made by the same student have been made through different paymentMethods over a 5-minute period */
    List<FeeTransaction> findByStudentIdAndTransactionTimeAndPaymentMethodNot(String studentId, LocalDateTime transactionTime, String paymentMethod);

}
