package com.genspark.order_service.services;

import com.genspark.order_service.dto.OrderHistoryReqRes;
import com.genspark.order_service.entities.OrderHistory;
import com.genspark.order_service.repositories.OrderHistoryRepository;
import com.genspark.order_service.util.OrderHistoryJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderHistoryServiceImp implements OrderHistoryService {
    private final OrderHistoryRepository orderHistoryRepo;
    private final OrderHistoryJWT jwtUtil;

    OrderHistoryServiceImp(OrderHistoryRepository orderHistoryRepo, OrderHistoryJWT jwtUtil){
        this.orderHistoryRepo = orderHistoryRepo;
        this.jwtUtil = jwtUtil;
    }


    @Override
    public OrderHistoryReqRes saveOrderHistory(OrderHistory orderHistory) {
        OrderHistoryReqRes reqRes = new OrderHistoryReqRes();
        try {
            OrderHistory newOrderHistory = this.orderHistoryRepo.save(orderHistory);
            if (newOrderHistory.getId() != null && !newOrderHistory.getId().isEmpty()){
                reqRes.setStatusCode(200);
                reqRes.setOrderHistory(newOrderHistory);
                reqRes.setMessage("Order Saved Successfully");
            } else {
                reqRes.setStatusCode(401);
                reqRes.setMessage("Order Saved Unsuccessfully");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error: " + e.getMessage());
        }
        return reqRes;
    }

    @Override
    public OrderHistoryReqRes getOrderHistoryById(String orderHistoryId) {
        OrderHistoryReqRes reqRes = new OrderHistoryReqRes();
        try {
            Optional<OrderHistory> orderHistoryOpt = this.orderHistoryRepo.findById(orderHistoryId);
            if (orderHistoryOpt.isPresent()) {
                reqRes.setStatusCode(200);
                reqRes.setOrderHistory(orderHistoryOpt.get());
                reqRes.setMessage("Order History Found");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("Order History Not Found");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error: " + e.getMessage());
        }
        return reqRes;
    }

    @Override
    public OrderHistoryReqRes getAllOrderHistoryByUserId(String userId) {
        OrderHistoryReqRes reqRes = new OrderHistoryReqRes();
        try {
            List<OrderHistory> orderHistoryList = this.orderHistoryRepo.findAllByUserId(userId);
            if (!orderHistoryList.isEmpty()) {
                reqRes.setStatusCode(200);
                reqRes.setOrderHistories(orderHistoryList);
                reqRes.setMessage("Order Histories Retrieved Successfully");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("No Order Histories Found for User");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error: " + e.getMessage());
        }
        return reqRes;
    }

    @Override
    public OrderHistoryReqRes deleteOrderHistory(String id) {
        OrderHistoryReqRes reqRes = new OrderHistoryReqRes();
        try {
            if (this.orderHistoryRepo.existsById(id)) {
                this.orderHistoryRepo.deleteById(id);
                reqRes.setStatusCode(200);
                reqRes.setMessage("Order History Deleted Successfully");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("Order History Not Found");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error: " + e.getMessage());
        }
        return reqRes;
    }
    @Override
    public String validateAndExtractUsername(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        } else { // make sure token is in the right format
            throw new IllegalArgumentException("Invalid token format");
        }

        if (!(jwtUtil.tokenValidate(token))) { // check if token still valid
            throw new SecurityException("Invalid or expired token");
        }

        String username = jwtUtil.extractUsername(token);
        if (username == null) { // make sure the token was encrypted with the username
            throw new IllegalStateException("Username could not be extracted from token");
        }

        return username;
    }

    @Override
    public List<String> validateAndExtractRole(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        } else { // make sure token is in the right format
            throw new IllegalArgumentException("Invalid token format");
        }

        if (!(jwtUtil.tokenValidate(token))) { // check if token still valid
            throw new SecurityException("Invalid or expired token");
        }

        List<String> roles = jwtUtil.extractRoles(token);
        if (roles == null) { // make sure the token was encrypted with the username
            throw new IllegalStateException("Username could not be extracted from token");
        }

        return roles;
    }
}
