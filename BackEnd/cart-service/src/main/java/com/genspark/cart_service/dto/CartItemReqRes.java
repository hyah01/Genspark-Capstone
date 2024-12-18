package com.genspark.cart_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.genspark.cart_service.model.CartItem;
import com.genspark.cart_service.model.CartItems;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CartItemReqRes {
    private int statusCode;
    private String message;
    private List<CartItem> items;
    private CartItems cartItems;
}
