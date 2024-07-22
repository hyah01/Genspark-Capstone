package com.genspark.order_service.services;

import com.genspark.order_service.entities.Transaction;
import com.genspark.order_service.repositories.TransactionRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImp implements TransactionService {
    @Autowired
    private TransactionRepository transactionRepo;

    @Override
    public Transaction saveTransaction(Transaction transaction) {
        return this.transactionRepo.save(transaction) ;
    }

    @Override
    public Transaction updateTransaction(Transaction transaction) {
        return this.transactionRepo.save(transaction) ;
    }

    @Override
    public Optional<Transaction> getTransactionById(String id) {
        return this.transactionRepo.findById(id);
    }

    @Override
    public boolean deleteTransactionById(String id) {
        if (transactionRepo.existsById(id)) {
            transactionRepo.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

}
