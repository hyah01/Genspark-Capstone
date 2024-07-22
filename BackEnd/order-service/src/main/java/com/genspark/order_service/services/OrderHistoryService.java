package com.genspark.order_service.services;

import com.genspark.order_service.entities.OrderHistory;

public interface OrderHistoryService {
    OrderHistory saveOrderHistory(OrderHistory orderHistory);
    OrderHistory getOrderHistoryWithTransactions(String orderHistoryId);
    boolean deleteOrderHistory(String id);
}
