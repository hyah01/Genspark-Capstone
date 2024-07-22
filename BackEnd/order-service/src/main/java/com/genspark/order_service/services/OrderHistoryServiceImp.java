package com.genspark.order_service.services;

import com.genspark.order_service.entities.OrderHistory;
import com.genspark.order_service.repositories.OrderHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderHistoryServiceImp implements OrderHistoryService {
    @Autowired
    private OrderHistoryRepository orderHistoryRepo;

    @Autowired
    private TransactionService transactionService;

    @Override
    public OrderHistory saveOrderHistory(OrderHistory orderHistory) {
        return this.orderHistoryRepo.save(orderHistory);
    }

    @Override
    public OrderHistory getOrderHistoryWithTransactions(String orderHistoryId) {
        return null;
    }


    @Override
    public boolean deleteOrderHistory(String id) {
        if (orderHistoryRepo.existsById(id)) {
            orderHistoryRepo.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
