package com.genspark.cart_service.controller;


import com.genspark.cart_service.dto.CartReqRes;
import com.genspark.cart_service.model.Cart;
import com.genspark.cart_service.services.CartService;
import org.apache.catalina.User;
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
    public ResponseEntity<CartReqRes> add(@RequestBody CartReqRes reg){
        CartReqRes reqRes = new CartReqRes();
        try {
            Cart cart = new Cart();
            cart.setUserId(reg.getUserId());
            cart.setCartOrder(reg.getCartOrder());
            Cart result = service.addCart(cart);
            if (result.getId() != null && !result.getId().isEmpty()){
                reqRes.setCart(cart);
                reqRes.setMessage("Successfully Created Cart");
                reqRes.setStatusCode(200);
            } else {
                reqRes.setStatusCode(500);
                reqRes.setMessage("Unsuccessfully Created Cart");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error: " + e);
        }
        return ResponseEntity.status(reqRes.getStatusCode()).body(reqRes);
    }

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
    public ResponseEntity<String> deleteById(@PathVariable String id){
        String string = service.deleteCart(id);
        if (string == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(string);
        }
    }

    // Exception handling for this controller
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + ex.getMessage());
    }
}
