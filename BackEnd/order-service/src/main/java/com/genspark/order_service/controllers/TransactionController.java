package com.genspark.order_service.controllers;

import com.genspark.order_service.entities.Transaction;
import com.genspark.order_service.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Transaction> saveTransaction(@RequestBody Transaction transaction) {
        Transaction savedTransaction = transactionService.saveTransaction(transaction);
        if (savedTransaction != null) {
            return new ResponseEntity<>(savedTransaction, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Transaction>> getTransactionById(@PathVariable String id) {
        Optional<Transaction> transaction = transactionService.getTransactionById(id);
        if (transaction.isPresent()) {
            return new ResponseEntity<>(transaction, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Transaction> updateTransaction(@RequestBody Transaction transaction) {
        Transaction updatedTransaction = transactionService.updateTransaction(transaction);
        if (updatedTransaction != null) {
            return new ResponseEntity<>(updatedTransaction, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable String id) {
        boolean isDeleted = transactionService.deleteTransactionById(id);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
