package com.genspark.cart_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.genspark.cart_service.model.CartItem;
import com.genspark.cart_service.model.CartItems;
import com.genspark.cart_service.model.SaveForLaterItem;
import com.genspark.cart_service.model.SaveForLaterItems;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SFLReqRes {
    private int statusCode;
    private String message;
    private List<SaveForLaterItem> items;
    private SaveForLaterItems sflItems;
}
