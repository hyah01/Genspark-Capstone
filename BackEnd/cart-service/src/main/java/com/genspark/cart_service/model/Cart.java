package com.genspark.cart_service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "cart")
public class Cart {
    @Id
    private String id;
    private String email; // To know which user the cart belong to
    private String cartItemsId; // Storing ID make it easier to retrieve and smaller size to contain
    private String saveForLaterId; // Storing ID make it easier to retrieve and smaller size to contain
    private String WishListId;
}