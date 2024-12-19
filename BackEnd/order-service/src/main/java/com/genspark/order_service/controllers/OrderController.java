package com.genspark.order_service.controllers;


import com.genspark.order_service.dto.OrderHistoryReqRes;
import com.genspark.order_service.entities.OrderHistory;
import com.genspark.order_service.services.OrderHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orderHistory")
public class OrderController {

    private final OrderHistoryService service;

    OrderController(OrderHistoryService service){
        this.service = service;
    }

    @GetMapping()
    public ResponseEntity<OrderHistoryReqRes> getOrderHistoryByUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        try {
            // Validate the token and extract the username
            String username = service.validateAndExtractUsername(token);
            // Fetch the cart using the username
            OrderHistoryReqRes reqRes = service.getAllOrderHistoryByUserId(username);
            return ResponseEntity.status(reqRes.getStatusCode()).body(reqRes);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @GetMapping("/user/{id}")
    public ResponseEntity<OrderHistoryReqRes> getOrderHistoryByUserId(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @PathVariable String id){
        try {
            // Validate the token and extract the username
            List<String> roles = service.validateAndExtractRole(token);
            if (roles.contains("ADMIN")){
                OrderHistoryReqRes reqRes = service.getAllOrderHistoryByUserId(id);
                return ResponseEntity.status(reqRes.getStatusCode()).body(reqRes);
            }
            else {
                OrderHistoryReqRes reqRes = new OrderHistoryReqRes();
                reqRes.setMessage("Not Authorized");
                reqRes.setStatusCode(HttpStatus.UNAUTHORIZED.value());
                return ResponseEntity.status(reqRes.getStatusCode()).body(reqRes);
            }

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderHistoryReqRes> getOrderHistoryById(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @PathVariable String id) {
        try {
            // Validate the token and extract the username
            String username = service.validateAndExtractUsername(token);
            // Fetch the cart using the username
            OrderHistoryReqRes reqRes = service.getOrderHistoryById(id);
            if (reqRes.getOrderHistory().getUserId().equals(username)){
                return ResponseEntity.status(reqRes.getStatusCode()).body(reqRes);
            }
            else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping()
    public ResponseEntity<OrderHistoryReqRes> saveOrderHistory(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody OrderHistory orderHistory) {
        try {
            // Validate the token and extract the username
            String username = service.validateAndExtractUsername(token);
            orderHistory.setUserId(username);
            OrderHistoryReqRes reqRes = service.saveOrderHistory(orderHistory);
            return ResponseEntity.status(reqRes.getStatusCode()).body(reqRes);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<OrderHistoryReqRes> deleteOrderHistory(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @PathVariable String id) {
        try {
            // Validate the token and extract the username
            String username = service.validateAndExtractUsername(token);
            OrderHistoryReqRes reqRes = service.getOrderHistoryById(id);
            if (username.equals(reqRes.getOrderHistory().getUserId())){
                reqRes = service.deleteOrderHistory(id);
                return ResponseEntity.status(reqRes.getStatusCode()).body(reqRes);
            }
            else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
