package com.genspark.cart_service.controller;


import com.genspark.cart_service.model.Cart;
import com.genspark.cart_service.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService service;


    @PostMapping // add to the database when account created
    public Cart add(@RequestBody Cart cart){ return service.addCart(cart);}

    @GetMapping("/{id}") // Get the cart by id
    public Cart findById(@PathVariable String id){
        return service.getCartByCartId(id);
    }

    @DeleteMapping("/{id}") // delete cart when account deleted
    public String deleteById(@PathVariable String id){
        return service.deleteCart(id);
    }






}
