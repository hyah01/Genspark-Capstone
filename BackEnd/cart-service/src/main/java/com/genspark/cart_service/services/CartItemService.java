package com.genspark.cart_service.services;

import com.genspark.cart_service.dto.CartItemReqRes;
import com.genspark.cart_service.model.CartItem;


public interface CartItemService {

    CartItemReqRes addCartItem();

    CartItemReqRes getCartItemById(String id);

    CartItemReqRes addItem(String id, CartItem cart);

    CartItemReqRes updateItem(String id, CartItem cart);

    CartItemReqRes deleteAllItem(String id, CartItem cart);
}
