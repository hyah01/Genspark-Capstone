package com.genspark.cart_service.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// This class store data of each product to be put in a bigger container
public class CartItem {
    private String productId; // ID of product
    private Integer quantity; // Quantity
}
