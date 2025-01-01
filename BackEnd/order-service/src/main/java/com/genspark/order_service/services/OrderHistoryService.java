package com.genspark.order_service.services;

import com.genspark.order_service.dto.OrderHistoryReqRes;
import com.genspark.order_service.entities.OrderHistory;

import java.util.List;

public interface OrderHistoryService {
    OrderHistoryReqRes saveOrderHistory(OrderHistory orderHistory);
    OrderHistoryReqRes getOrderHistoryById(String orderHistoryId);

    OrderHistoryReqRes getAllOrderHistoryByUserId(String userId);
    OrderHistoryReqRes deleteOrderHistory(String id);
    String validateAndExtractUsername(String token);
    List<String> validateAndExtractRole(String token);
}
