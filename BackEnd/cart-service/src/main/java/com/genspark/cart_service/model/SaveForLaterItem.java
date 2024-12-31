package com.genspark.cart_service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// This class store data of each product to be put in a bigger container
public class SaveForLaterItem {
    private String productId; // ID of product
    private Integer quantity;
}
