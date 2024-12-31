package com.genspark.cart_service.controller;

import com.genspark.cart_service.dto.CartItemReqRes;
import com.genspark.cart_service.dto.SFLReqRes;
import com.genspark.cart_service.model.CartItem;
import com.genspark.cart_service.model.SaveForLaterItem;
import com.genspark.cart_service.model.SaveForLaterItems;
import com.genspark.cart_service.services.CartItemService;
import com.genspark.cart_service.services.CartService;
import com.genspark.cart_service.services.SaveForLaterService;
import com.genspark.cart_service.util.CartJwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/saveforlater")
public class SaveForLaterController {
    private final SaveForLaterService service;
    private final CartService cartService;

    public SaveForLaterController(SaveForLaterService service, CartService cartService) {
        this.service = service;
        this.cartService = cartService;
    }

    @PutMapping("/add-item") // Add a single order to the databased
    public ResponseEntity<SFLReqRes> addItem(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody SaveForLaterItem cartOrder){
        try {
            // Validate the token and extract the username
            String username = cartService.validateAndExtractUsername(token);
            // Get Cart Items ID
            String cartItemId = cartService.getCartByEmail(username).getSaveForLaterId();
            // Fetch the cart using the username
            SFLReqRes reqRes = service.addItem(cartItemId, cartOrder);
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
    public ResponseEntity<SFLReqRes> addItem(@RequestHeader(HttpHeaders.AUTHORIZATION) String token){
        try {
            // Validate the token and extract the username
            String username = cartService.validateAndExtractUsername(token);
            // Get Cart Items ID
            String cartItemId = cartService.getCartByEmail(username).getSaveForLaterId();
            // Fetch the cart using the username
            SFLReqRes reqRes = service.getSFLItemById(cartItemId);
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
    public ResponseEntity<SFLReqRes> updateItem(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody SaveForLaterItem cartOrder){
        try {
            // Validate the token and extract the username
            String username = cartService.validateAndExtractUsername(token);
            // Get Cart Items ID
            String cartItemId = cartService.getCartByEmail(username).getSaveForLaterId();
            // Fetch the cart using the username
            SFLReqRes reqRes = service.updateItem(cartItemId, cartOrder);
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
    public ResponseEntity<SFLReqRes> deleteAllItems(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody SaveForLaterItem cartOrder){
        try {
            // Validate the token and extract the username
            String username = cartService.validateAndExtractUsername(token);
            // Get Cart Items ID
            String cartItemId = cartService.getCartByEmail(username).getSaveForLaterId();
            // Fetch the cart using the username
            SFLReqRes reqRes = service.deleteAllItem(username, cartOrder);
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
