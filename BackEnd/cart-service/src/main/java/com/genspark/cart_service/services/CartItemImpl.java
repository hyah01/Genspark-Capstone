package com.genspark.cart_service.services;

import com.genspark.cart_service.dto.CartItemReqRes;
import com.genspark.cart_service.model.CartItem;
import com.genspark.cart_service.model.CartItems;
import com.genspark.cart_service.repository.CartItemsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class CartItemImpl implements CartItemService {

    @Autowired
    private CartItemsRepository repository;


    @Override
    public CartItemReqRes addCartItem() {
        CartItemReqRes reqRes = new CartItemReqRes();
        try {
            // Create new Cart Items
            CartItems cartItems = new CartItems();
            cartItems.setItems(new HashMap<>()); // Initialize the list
            // Save the cart
            CartItems result = repository.save(cartItems);
            // If cart was created successfully
            if (result.getId() != null && !result.getId().isEmpty()){
                reqRes.setCartItems(result);
                reqRes.setMessage("Successfully Created Item Cart");
                reqRes.setStatusCode(200);
            } else {
                // If cart didn't create successfully
                reqRes.setStatusCode(500);
                reqRes.setMessage("Unsuccessfully Created Item Cart");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error: " + e.getMessage());
        }
        return reqRes;
    }

    @Override
    public CartItemReqRes getCartItemById(String id) {
        CartItemReqRes reqRes = new CartItemReqRes();
        try {
            CartItems cartItems = repository.findById(id).orElseThrow(() -> new RuntimeException("Cart Not Found"));
            reqRes.setCartItems(cartItems);
            reqRes.setStatusCode(200);
            reqRes.setMessage("Cart with id '" + id + "' found successfully");
        } catch (Exception e){
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error Occurred: " + e.getMessage());
        }
        return  reqRes;
    }

    @Override
    public CartItemReqRes addItem(String id, CartItem cartOrder) {
        CartItemReqRes reqRes = new CartItemReqRes();
        try {
            // Fetch the Item Cart
            CartItems cartItems = getCartItemById(id).getCartItems();

            // Ensure the map is initialized
            Map<String, CartItem> items = cartItems.getItems();
            if (items == null) {
                items = new HashMap<>();
                cartItems.setItems(items);
            }

            // Check if the item already exists in the cart
            CartItem existingItem = items.get(cartOrder.getProductId());
            if (existingItem != null) {
                // Update the quantity
                int updatedQuantity = existingItem.getQuantity() + cartOrder.getQuantity();
                if (updatedQuantity <= 0) {
                    // Remove the item if quantity is zero or less
                    items.remove(cartOrder.getProductId());
                } else {
                    // Update the quantity
                    existingItem.setQuantity(updatedQuantity);
                }
            } else if (cartOrder.getQuantity() > 0) {
                // Add new item if it doesn't exist and quantity > 0
                items.put(cartOrder.getProductId(), cartOrder);
            }

            // Save the updated cart
            repository.save(cartItems);

            reqRes.setCartItems(cartItems);
            reqRes.setStatusCode(200);
            reqRes.setMessage("Successfully updated cart");
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error Occurred: " + e.getMessage());
        }
        return reqRes;
    }
    @Override
    public CartItemReqRes updateItem(String id, CartItem cartOrder){
        CartItemReqRes reqRes = new CartItemReqRes();
        try {
            // Fetch the cart
            // Fetch the Item Cart
            CartItems cartItems = getCartItemById(id).getCartItems();

            // Ensure the map is initialized
            Map<String, CartItem> items = cartItems.getItems();
            if (items == null) {
                items = new HashMap<>();
                cartItems.setItems(items);
            }

            if (cartOrder.getQuantity() <= 0){
                items.remove(cartOrder.getProductId());
            } else {
                items.get(cartOrder.getProductId()).setQuantity(cartOrder.getQuantity());
            }
            // Save the updated cart
            repository.save(cartItems);
            reqRes.setCartItems(cartItems);
            reqRes.setStatusCode(200);
            return reqRes;
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error Occurred: " + e.getMessage());
            return reqRes;
        }
    }

    @Override
    public CartItemReqRes deleteAllItem(String id, CartItem cartOrder){
        CartItemReqRes reqRes = new CartItemReqRes();
        try {
            // Fetch the Item Cart
            CartItems cartItems = getCartItemById(id).getCartItems();

            // Ensure the map is initialized
            Map<String, CartItem> items = cartItems.getItems();
            if (items != null) {
                items.clear();
            }

            // Save the updated cart
            repository.save(cartItems);
            reqRes.setCartItems(cartItems);
            reqRes.setStatusCode(200);
            return reqRes;
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error Occurred: " + e.getMessage());
            return reqRes;
        }
    }
}
