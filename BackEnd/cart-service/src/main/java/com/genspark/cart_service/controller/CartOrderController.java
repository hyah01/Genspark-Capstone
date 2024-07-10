package com.genspark.cart_service.controller;

import com.genspark.cart_service.model.CartOrder;
import com.genspark.cart_service.services.CartOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cartOrder")
public class CartOrderController {
    @Autowired
    private CartOrderService service;


    @PostMapping
    public CartOrder add(@RequestBody CartOrder cartOrder){
        return service.addCartOrder(cartOrder);
    }

    @PutMapping
    public CartOrder update(@RequestBody CartOrder cartOrder){
        CartOrder updated = service.getById(cartOrder.getId());
        updated.setQuantity(cartOrder.getQuantity());
        return service.updateCartOrder(updated);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable String id){
        return service.deleteCartOrder(id);
    }

    @GetMapping
    public List<CartOrder> findAll(){
        return service.getAllCartOrder();
    }

    @GetMapping("/{id}")
    public List<CartOrder> getByCartId(String id){
        return service.getAllCartOrderByCartId(id);
    }

    @GetMapping("/byId/{id}")
    public CartOrder getById(String id){
        return service.getById(id);
    }

}
