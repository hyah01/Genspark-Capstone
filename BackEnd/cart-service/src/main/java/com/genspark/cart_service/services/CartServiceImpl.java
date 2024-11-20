package com.genspark.cart_service.services;

import com.genspark.cart_service.dto.CartReqRes;
import com.genspark.cart_service.model.Cart;
import com.genspark.cart_service.model.CartOrder;
import com.genspark.cart_service.repository.CartRepository;
import com.genspark.cart_service.util.CartJwtUtil;
import com.genspark.user_service.dto.ReqRes;
import com.genspark.user_service.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService{

    @Autowired
    private CartRepository repository;

    @Autowired
    private CartJwtUtil jwtUtil;


    @Override
    public CartReqRes addCart(CartReqRes reg) {
        CartReqRes reqRes = new CartReqRes();
        try {
            Cart cart = new Cart();
            cart.setEmail(reg.getEmail());
            cart.setCartOrder(reg.getCartOrder());
            Cart result = repository.save(cart);
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
        return reqRes;
    }

    @Override // Return Null if item of id cannot be found
    public CartReqRes getCartByCartId(String cartId) {
        CartReqRes reqRes = new CartReqRes();
        try {
            Cart cart = repository.findById(cartId).orElseThrow(() -> new RuntimeException("Cart Not Found"));
            reqRes.setCart(cart);
            reqRes.setStatusCode(200);
            reqRes.setMessage("Cart with id '" + cartId + "' found successfully");
        } catch (Exception e){
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error Occurred: " + e.getMessage());
        }
        return  reqRes;
    }

    @Override
    public CartReqRes addItem(String username, CartOrder cartOrder){
        CartReqRes reqRes = new CartReqRes();
        try {
            // Fetch the cart
            Cart cart = getCartByEmail(username).getCart();
            // If cart not
            if (cart == null){
                reqRes.setEmail(username);
                reqRes.setCartOrder(new HashMap<String,Integer>());
                cart = addCart(reqRes).getCart();
            }

            if (cart.getCartOrder().containsKey(cartOrder.getProductId())){
                cart.getCartOrder().put(cartOrder.getProductId(), (cartOrder.getQuantity() + cart.getCartOrder().get(cartOrder.getProductId())));
            } else {
                cart.getCartOrder().put(cartOrder.getProductId(), cartOrder.getQuantity());
            }

            updateCart(cart);
            reqRes.setCart(cart);
            reqRes.setStatusCode(200);
            return reqRes;
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error Occurred: " + e.getMessage());
            return reqRes;
        }
    }
    @Override
    public CartReqRes updateItem(String username, CartOrder cartOrder){
        CartReqRes reqRes = new CartReqRes();
        try {
            // Fetch the cart
            Cart cart = getCartByEmail(username).getCart();
            if (cartOrder.getQuantity() == 0){
                cart.getCartOrder().remove(cartOrder.getProductId());
            } else {
                cart.getCartOrder().put(cartOrder.getProductId(), (cartOrder.getQuantity()));
            }
            updateCart(cart);
            reqRes.setCart(cart);
            reqRes.setStatusCode(200);
            return reqRes;
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error Occurred: " + e.getMessage());
            return reqRes;
        }
    }

    @Override
    public CartReqRes deleteAllItem(String username, CartOrder cartOrder){
        CartReqRes reqRes = new CartReqRes();
        try {
            // Fetch the cart
            Cart cart = getCartByEmail(username).getCart();
            cart.setCartOrder(new HashMap<String,Integer>());
            updateCart(cart);
            reqRes.setCart(cart);
            reqRes.setStatusCode(200);
            return reqRes;
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error Occurred: " + e.getMessage());
            return reqRes;
        }
    }


    @Override
    public Cart updateCart(Cart cart) {
        return repository.save(cart);
    }

    @Override
    public CartReqRes deleteCart(String cartId) {
        CartReqRes reqRes = new CartReqRes();
        try {
            if (repository.existsById(cartId)){
                repository.deleteById(cartId);
                reqRes.setStatusCode(200);
                reqRes.setMessage("Cart with id '" + cartId + "' deleted successfully");

            } else {
                throw new RuntimeException("Cart Not Found");
            }
        } catch (Exception e){
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error Occurred: " + e.getMessage());
        }
        return  reqRes;
    }





    @Override
    public CartReqRes getCartByEmail(String email) {
        CartReqRes reqRes = new CartReqRes();
        try {
            Cart cart = repository.findByEmail(email).orElseThrow(() -> new RuntimeException("Cart Not Found"));
            reqRes.setCart(cart);
            reqRes.setStatusCode(200);
            reqRes.setMessage("Cart with email '" + email + "' found successfully");
        } catch (Exception e){
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error Occurred: " + e.getMessage());
        }
        return  reqRes;
    }

    @Override
    public CartReqRes hasCart(String email){
        CartReqRes reqRes = new CartReqRes();
        try{
            if (repository.findByEmail(email).isPresent()){
                reqRes.setMessage("true");
                reqRes.setStatusCode(200);
            } else {
                reqRes.setMessage("false");
                reqRes.setStatusCode(200);
            }
        } catch (Exception e){
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error Occurred: " + e.getMessage());
        }
        return reqRes;
    }

    @Override
    public String validateAndExtractUsername(String token) throws Exception {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        } else { // make sure token is in the right format
            throw new IllegalArgumentException("Invalid token format");
        }

        if (!(jwtUtil.tokenValidate(token))) { // check if token still valid
            throw new SecurityException("Invalid or expired token");
        }

        String username = jwtUtil.extractUsername(token);
        if (username == null) { // make sure the token was encrypted with the username
            throw new IllegalStateException("Username could not be extracted from token");
        }

        return username;
    }
}
