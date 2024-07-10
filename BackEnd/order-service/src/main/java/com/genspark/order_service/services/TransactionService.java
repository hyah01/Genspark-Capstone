package com.genspark.order_service.services;

import com.genspark.order_service.entities.Transaction;

import java.util.Optional;

public interface TransactionService {
    Transaction saveTransaction(Transaction transaction);
    Optional<Transaction> getTransactionById(String id);
    void deleteTransactionById(String id);
    Transaction updateTransaction(Transaction transaction);

}
