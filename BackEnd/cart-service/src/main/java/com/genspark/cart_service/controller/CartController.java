package com.genspark.cart_service.controller;


import com.genspark.cart_service.model.Cart;
import com.genspark.cart_service.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/{id}")
    public ResponseEntity<Cart> getCartByID(@PathVariable String id) {
        Cart cart = service.getCartByCartId(id);
        if (cart == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(cart);
        }
    }

    @DeleteMapping("/{id}") // delete cart when account deleted
    public String deleteById(@PathVariable String id){
        return service.deleteCart(id);
    }

    // Exception handling for this controller
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + ex.getMessage());
    }
}
