package com.genspark.cart_service.services;

import com.genspark.cart_service.model.Cart;
import org.springframework.stereotype.Service;

public interface CartService {

    Cart getCartByCartId(String cartId);

    Cart addCart(Cart cart);

    Cart updateCart(Cart cart);

    Cart getCartByEmail(String email);

    Boolean hasCart(String email);

    String deleteCart(String cartId);





}
