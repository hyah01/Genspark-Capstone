package com.genspark.order_service.services;

import com.genspark.order_service.entities.Transaction;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Optional;

public interface TransactionService {
    Transaction saveTransaction(Transaction transaction);
    Optional<Transaction> getTransactionById(String id);
    boolean deleteTransactionById(String id);
    Transaction updateTransaction(Transaction transaction);

}
