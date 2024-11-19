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
        CartReqRes reqRes = new CartReqRes();
        try {
            Cart cart = new Cart();
            cart.setEmail(reg.getEmail());
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

    @GetMapping("has-cart/{email}")
    public ResponseEntity<CartReqRes> hasCart(@PathVariable String email){
        CartReqRes reqRes = new CartReqRes();
        reqRes.setMessage(service.hasCart(email).toString());
        reqRes.setStatusCode(200);

        return ResponseEntity.status(reqRes.getStatusCode()).body(reqRes);
    }

    @GetMapping("/get-my-cart")
    public ResponseEntity<Cart> getMyCart(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            // Validate the token
            String username = jwtUtil.extractUsername(token);
            System.out.println(username);
            if (username == null || jwtUtil.tokenValidate(token)) {
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
            }

            // Fetch the cart
            Cart cart = service.getCartByEmail(username);
            return ResponseEntity.ok(cart);
        } catch (Exception e) {
            e.printStackTrace(); // Print stack trace for debugging
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
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

    @PutMapping("/add-item") // Add a single order to the databased
    public ResponseEntity<CartOrder> add(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody CartOrder cartOrder){
        // TO DO: Make this more module by making token validation and getting user name to be a method
        // Move all the logic to the services

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            // Validate the token
            String username = jwtUtil.extractUsername(token);
            System.out.println(username);
            if (username == null || jwtUtil.tokenValidate(token)) {
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
            }

            // Fetch the cart
            Cart cart = service.getCartByEmail(username);
            boolean flag = true;
            // check if product inside
            for (CartOrder order: cart.getCartOrder()){
                System.out.println(cartOrder.getProductId());
                if (order.getProductId().equals(cartOrder.getProductId())){
                    flag = false;
                    order.setQuantity(order.getQuantity() + cartOrder.getQuantity());
                    break;
                }
            }
            if (flag){
                cart.getCartOrder().add(cartOrder);
            }
            service.updateCart(cart);

            return ResponseEntity.ok(cartOrder);
        } catch (Exception e) {
            e.printStackTrace(); // Print stack trace for debugging
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    // Exception handling for this controller
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + ex.getMessage());
    }
}
