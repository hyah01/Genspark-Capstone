package com.genspark.cart_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.genspark.cart_service.model.Cart;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CartReqRes {
    private int statusCode;
    private String message;
    private String email;
    private Cart cart;
    private String cartItemsId;
    private String saveForLaterId;
    private String wishListId;
}
