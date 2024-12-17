package com.genspark.cart_service.controller;

import com.genspark.cart_service.dto.CartItemReqRes;
import com.genspark.cart_service.model.CartItem;
import com.genspark.cart_service.services.CartItemService;
import com.genspark.cart_service.services.CartService;
import com.genspark.cart_service.util.CartJwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart-item")
public class CartItemsController {
    @Autowired
    private CartItemService service;

    @Autowired
    private CartJwtUtil jwtUtil;

    @Autowired
    private CartService cartService;

    @PutMapping("/add-item") // Add a single order to the databased
    public ResponseEntity<CartItemReqRes> addItem(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody CartItem cartOrder){
        try {
            // Validate the token and extract the username
            String username = cartService.validateAndExtractUsername(token);
            // Get Cart Items ID
            String cartItemId = cartService.getCartByEmail(username).getCartItemsId();
            // Fetch the cart using the username
            CartItemReqRes reqRes = service.addItem(cartItemId, cartOrder);
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

    @GetMapping("/get-item") // Add a single order to the databased
    public ResponseEntity<CartItemReqRes> addItem(@RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        try {
            // Validate the token and extract the username
            String username = cartService.validateAndExtractUsername(token);
            // Get Cart Items ID
            String cartItemId = cartService.getCartByEmail(username).getCartItemsId();
            // Fetch the cart using the username
            CartItemReqRes reqRes = service.getCartItemById(cartItemId);
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
    public ResponseEntity<CartItemReqRes> updateItem(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody CartItem cartOrder){
        try {
            // Validate the token and extract the username
            String username = cartService.validateAndExtractUsername(token);
            // Get Cart Items ID
            String cartItemId = cartService.getCartByEmail(username).getCartItemsId();
            // Fetch the cart using the username
            CartItemReqRes reqRes = service.updateItem(cartItemId, cartOrder);
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
    @PutMapping("/delete-all-items")
    public ResponseEntity<CartItemReqRes> deleteAllItems(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody CartItem cartOrder){
        try {
            // Validate the token and extract the username
            String username = cartService.validateAndExtractUsername(token);
            // Get Cart Items ID
            String cartItemId = cartService.getCartByEmail(username).getCartItemsId();
            // Fetch the cart using the username
            CartItemReqRes reqRes = service.deleteAllItem(username, cartOrder);
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

    @PutMapping("/checkout")
    public ResponseEntity<CartItemReqRes> checkout(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody CartItem cartOrder){
        try {
            // Validate the token and extract the username
            String username = cartService.validateAndExtractUsername(token);
            // Get Cart Items ID
            String cartItemId = cartService.getCartByEmail(username).getCartItemsId();
            // Fetch the cart using the username
            CartItemReqRes reqRes = service.deleteAllItem(username, cartOrder);
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
