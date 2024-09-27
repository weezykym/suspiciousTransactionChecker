package com.interview.FraudDetectionSystem.service;

import com.interview.FraudDetectionSystem.entity.FeeTransaction;
import com.interview.FraudDetectionSystem.repository.FeeTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class FeeTransactionService {

    private final FeeTransactionRepository feeTransactionRepository;

    @Autowired
    public FeeTransactionService(FeeTransactionRepository feeTransactionRepository) {
        this.feeTransactionRepository = feeTransactionRepository;
    }

    //1. Duplicate Transaction Check
    public List<FeeTransaction> checkForDuplicateTransactions(FeeTransaction transaction) {
        LocalDateTime startTime = transaction.getTransactionTime().minusMinutes(10);
        LocalDateTime endTime = transaction.getTransactionTime().plusMinutes(10);
        return feeTransactionRepository
                .findByAmountAndPaymentMethodAndTransactionTimeBetween(transaction.getAmount(), transaction.getPaymentMethod(), startTime, endTime);
    }

    //2. Checking for high frequency payments
    public List<FeeTransaction> checkForHighPaymentfrequency(String studentId, LocalDateTime transactionTime) {
        LocalDateTime startTime = transactionTime.minusMinutes(10);
        LocalDateTime endTime = transactionTime;

        List<FeeTransaction> recentTransactions = feeTransactionRepository.findByStudentIdAndTransactionTimeBetween(studentId, startTime, endTime);
        if (recentTransactions.size() > 3) {
            return recentTransactions;
        }
        return recentTransactions = null;
    }

    //3. Payment Clustering check
    public List<FeeTransaction> checkForPaymentClustering(String studentId, String paymentMethod, LocalDateTime transactionTime, Double initialBalance) {
        LocalDateTime timeFrame = transactionTime.minusMinutes(5);
        List<FeeTransaction> closeTransactions = feeTransactionRepository.findByStudentIdAndTransactionTimeAndPaymentMethodNot(studentId, transactionTime, paymentMethod);

        //filtering the transactions where the amount is greater than 30% of initialBalance and happened within the timeframe using different methods
        return closeTransactions.stream().filter(t -> t.getAmount() > (0.3 * initialBalance) && t.getTransactionTime().isAfter(timeFrame)).toList();
    }
    //4. Save a transaction
    public FeeTransaction saveTransaction(FeeTransaction transaction) {
        return feeTransactionRepository.save(transaction);
    }

    //5. Get all transactioons
    public List<FeeTransaction> getAllTransactions() {
        return feeTransactionRepository.findAll();
    }

}
