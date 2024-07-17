package com.genspark.cart_service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "cart")
public class Cart {
    @Id
    private String id;
    private String userId; // To know which user the cart belong to
    private List<CartOrder> cartOrder; // Contain all items user added to their cart
}