package com.genspark.cart_service.services;

import com.genspark.cart_service.model.Cart;
import org.springframework.stereotype.Service;

public interface CartService {

    Cart getCartByCartId(String cartId);

    Cart addCart(Cart cart);

    String deleteCart(String cartId);





}
