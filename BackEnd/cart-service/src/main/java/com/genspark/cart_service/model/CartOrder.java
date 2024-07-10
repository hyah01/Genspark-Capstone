package com.genspark.cart_service.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "cartOrder")
public class CartOrder {
    @Id
    private String id;
    private String cartId;
    private String productId;
    private Integer quantity;
}
