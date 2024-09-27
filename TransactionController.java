package com.interview.FraudDetectionSystem.controller;

import com.interview.FraudDetectionSystem.entity.FeeTransaction;
import com.interview.FraudDetectionSystem.service.FeeTransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final FeeTransactionService feeTransactionService;

    public TransactionController(FeeTransactionService feeTransactionService) {
        this.feeTransactionService = feeTransactionService;
    }

    //1. Endpoint to add a new transaction
    @PostMapping("/add")
    public ResponseEntity<FeeTransaction> addTransaction(@RequestBody FeeTransaction feeTransaction) {
        FeeTransaction savedTransaction = feeTransactionService.saveTransaction(feeTransaction);

        List<FeeTransaction> duplicateTransactions = feeTransactionService.checkForDuplicateTransactions(savedTransaction);
        List<FeeTransaction> highFrequencyTransactions = feeTransactionService.checkForHighPaymentfrequency(savedTransaction.getStudentId(), savedTransaction.getTransactionTime());
        List<FeeTransaction> clusteredTransactions = feeTransactionService
                .checkForPaymentClustering(savedTransaction.getStudentId(),
                        savedTransaction.getPaymentMethod(),
                        savedTransaction.getTransactionTime(),
                        savedTransaction.getInitialBalance());

        if (!duplicateTransactions.isEmpty() || !highFrequencyTransactions.isEmpty() || !clusteredTransactions.isEmpty()) {
            savedTransaction.setIsSuspicious(true);
            feeTransactionService.saveTransaction(savedTransaction);
        }

        return ResponseEntity.ok(savedTransaction);
    }
    @GetMapping("/suspiciousTransactions")
    public ResponseEntity<List<FeeTransaction>> getSuspiciousTransactions() {
        List<FeeTransaction> suspiciousTransactions = feeTransactionService.getAllTransactions().stream()
                .filter(susTransaction -> susTransaction.getIsSuspicious()).toList();
        return ResponseEntity.ok(suspiciousTransactions);
    }
}
