package com.genspark.cart_service.services;

import com.genspark.cart_service.dto.CartReqRes;
import com.genspark.cart_service.model.Cart;
import com.genspark.cart_service.model.CartOrder;
import org.springframework.stereotype.Service;

public interface CartService {

    CartReqRes getCartByCartId(String cartId);

    CartReqRes addCart(CartReqRes cart);

    Cart updateCart(Cart cart);

    CartReqRes getCartByEmail(String email);

    CartReqRes hasCart(String email);

    CartReqRes deleteCart(String cartId);

    String validateAndExtractUsername(String token) throws Exception;

    CartReqRes addItem(String username, CartOrder cart);

    CartReqRes updateItem(String username, CartOrder cart);

    CartReqRes deleteAllItem(String username, CartOrder cart);






}
