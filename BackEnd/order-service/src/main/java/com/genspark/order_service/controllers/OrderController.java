package com.genspark.order_service.controllers;


import com.genspark.order_service.entities.OrderHistory;
import com.genspark.order_service.services.OrderHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orderHistory")
public class OrderController {

    @Autowired
    private OrderHistoryService orderHistoryService;

    @GetMapping("/{id}/transactions") // Gets all orders by transaction Id
    public ResponseEntity<OrderHistory> getOrderHistoryByTransactionID(@PathVariable String id) {
        OrderHistory orderHistory = orderHistoryService.getOrderHistoryWithTransactions(id);
        if (orderHistory != null) {
            return new ResponseEntity<>(orderHistory, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<OrderHistory> saveOrderHistory(@RequestBody OrderHistory orderHistory) {
        OrderHistory savedOrderHistory = orderHistoryService.saveOrderHistory(orderHistory);
        if (savedOrderHistory != null) {
            return new ResponseEntity<>(savedOrderHistory, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteOrderHistory(@PathVariable String id) {
        boolean isDeleted = orderHistoryService.deleteOrderHistory(id);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
