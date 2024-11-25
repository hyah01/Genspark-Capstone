package com.genspark.cart_service.services;

import com.genspark.cart_service.dto.CartReqRes;
import com.genspark.cart_service.model.Cart;
import com.genspark.cart_service.repository.CartRepository;
import com.genspark.cart_service.util.CartJwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService{

    @Autowired
    private CartRepository repository;

    @Autowired
    private CartJwtUtil jwtUtil;

    @Autowired
    private CartItemImpl cartItems;


    @Override
    public CartReqRes addCart(CartReqRes reg) {
        CartReqRes reqRes = new CartReqRes();
        try {
            // Create new Cart
            Cart cart = new Cart();
            cart.setEmail(reg.getEmail());
            cart.setCartItemsId(cartItems.addCartItem().getCartItems().getId());
            // Save the cart
            Cart result = repository.save(cart);
            // If cart was created successfully
            if (result.getId() != null && !result.getId().isEmpty()){
                reqRes.setCart(cart);
                reqRes.setMessage("Successfully Created Cart");
                reqRes.setStatusCode(200);
            } else {
                // If cart didn't create successfully
                reqRes.setStatusCode(500);
                reqRes.setMessage("Unsuccessfully Created Cart");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error: " + e);
        }
        return reqRes;
    }

    @Override
    public CartReqRes getCartByCartId(String cartId) {
        CartReqRes reqRes = new CartReqRes();
        try {
            // fetch cart
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
            reqRes.setEmail(email);
            reqRes.setCartItemsId(cart.getCartItemsId());
            reqRes.setSaveForLaterId(cart.getSaveForLaterId());
            reqRes.setWishListId(cart.getWishListId());
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
