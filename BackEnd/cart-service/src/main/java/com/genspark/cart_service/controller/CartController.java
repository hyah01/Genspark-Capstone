package com.genspark.cart_service.controller;


import com.genspark.cart_service.dto.CartReqRes;
import com.genspark.cart_service.model.Cart;
import com.genspark.cart_service.model.CartOrder;
import com.genspark.cart_service.services.CartService;
import com.genspark.cart_service.services.CartUserInfoService;
import com.genspark.cart_service.util.CartJwtUtil;
import com.genspark.user_service.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    private static final Logger logger = LoggerFactory.getLogger(CartController.class);


    @Autowired
    private CartService service;

    @Autowired
    private CartJwtUtil jwtUtil;

    @Autowired
    private CartUserInfoService cartUserInfoService;


    @PostMapping // add to the database when account created
    public ResponseEntity<CartReqRes> add(@RequestBody CartReqRes reg){
        CartReqRes reqRes = service.addCart(reg);
        return ResponseEntity.status(reqRes.getStatusCode()).body(reqRes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartReqRes> getCartByID(@PathVariable String id) {
        return ResponseEntity.ok(service.getCartByCartId(id));
    }

    @GetMapping("has-cart/{email}")
    public ResponseEntity<CartReqRes> hasCart(@PathVariable String email){
        CartReqRes reqRes = service.hasCart(email);
        return ResponseEntity.status(reqRes.getStatusCode()).body(reqRes);
    }

    @GetMapping("/get-my-cart")
    public ResponseEntity<CartReqRes> getMyCart(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        try {
            // Validate the token and extract the username
            String username = service.validateAndExtractUsername(token);

            // Fetch the cart using the username
            CartReqRes reqRes = service.getCartByEmail(username);
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


    @DeleteMapping("/{id}") // delete cart when account deleted
    public ResponseEntity<CartReqRes> deleteById(@PathVariable String id){
        CartReqRes ReqRes = service.deleteCart(id);
        return ResponseEntity.status(ReqRes.getStatusCode()).body(ReqRes);
    }

    @PutMapping("/add-item") // Add a single order to the databased
    public ResponseEntity<CartReqRes> addItem(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody CartOrder cartOrder){
        try {
            // Validate the token and extract the username
            String username = service.validateAndExtractUsername(token);
            // Fetch the cart using the username
            CartReqRes reqRes = service.addItem(username, cartOrder);
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

    @PutMapping("/update-item") // Add a single order to the databased
    public ResponseEntity<CartReqRes> updateItem(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody CartOrder cartOrder){
        try {
            // Validate the token and extract the username
            String username = service.validateAndExtractUsername(token);
            // Fetch the cart using the username
            CartReqRes reqRes = service.updateItem(username, cartOrder);
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
    @PutMapping("/delete-all-items") // Add a single order to the databased
    public ResponseEntity<CartReqRes> deleteAllItems(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody CartOrder cartOrder){
        try {
            // Validate the token and extract the username
            String username = service.validateAndExtractUsername(token);
            // Fetch the cart using the username
            CartReqRes reqRes = service.deleteAllItem(username, cartOrder);
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


    // Exception handling for this controller
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + ex.getMessage());
    }
}
