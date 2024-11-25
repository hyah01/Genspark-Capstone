package com.genspark.cart_service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "cartItems")
public class CartItems {
    @Id
    private String id; // Unique ID for the cart items
    private Map<String, CartItem> items = new HashMap<>(); // List of cart items
}
