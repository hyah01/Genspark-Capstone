package com.genspark.cart_service.controller;


import com.genspark.cart_service.dto.CartReqRes;
import com.genspark.cart_service.model.CartItem;
import com.genspark.cart_service.services.CartService;
import com.genspark.cart_service.services.CartUserInfoService;
import com.genspark.cart_service.util.CartJwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService service;


    @PostMapping // add to the database when account created
    public ResponseEntity<CartReqRes> add(@RequestBody CartReqRes reg){
        CartReqRes reqRes = service.addCart(reg);
        return ResponseEntity.status(reqRes.getStatusCode()).body(reqRes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartReqRes> getCartByID(@PathVariable String id) {
        CartReqRes reqRes = service.getCartByCartId(id);
        return ResponseEntity.status(reqRes.getStatusCode()).body(reqRes);
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



    // Exception handling for this controller
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + ex.getMessage());
    }
}
