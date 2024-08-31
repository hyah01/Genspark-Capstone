package com.genspark.cart_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.genspark.cart_service.model.Cart;
import com.genspark.cart_service.model.CartOrder;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CartReqRes {
    private int statusCode;
    private String message;
    private String userId;
    private List<CartOrder> cartOrder;
    private Cart cart;
}
