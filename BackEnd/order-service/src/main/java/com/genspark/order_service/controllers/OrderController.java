package com.genspark.order_service.controllers;


import com.genspark.order_service.entities.OrderHistory;
import com.genspark.order_service.services.OrderHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orderHistory")
public class OrderController {

    @Autowired
    private OrderHistoryService orderHistoryService;

    @GetMapping("/{id}/transactions")
    public OrderHistory getOrderHistoryByTransactionID(@PathVariable String id) {
        return orderHistoryService.getOrderHistoryWithTransactions(id);
    }

    @PostMapping("/save")
    public OrderHistory saveOrderHistory(@RequestBody OrderHistory orderHistory) {
        return orderHistoryService.saveOrderHistory(orderHistory);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteOrderHistory(@PathVariable String id) {
        orderHistoryService.deleteOrderHistory(id);
    }
}
