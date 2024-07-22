package com.genspark.order_service.services;

import com.genspark.order_service.entities.OrderHistory;
import com.genspark.order_service.entities.Transaction;

import java.util.List;

public interface OrderHistoryService {
    OrderHistory saveOrderHistory(OrderHistory orderHistory);
    OrderHistory getOrderHistoryWithTransactions(String orderHistoryId);
    void deleteOrderHistory(String id);
}
