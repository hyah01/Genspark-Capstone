package com.genspark.cart_service.services;

import com.genspark.cart_service.model.CartOrder;
import org.springframework.stereotype.Service;

import java.util.List;


public interface CartOrderService {

    List<CartOrder> getAllCartOrder();

    List<CartOrder> getAllCartOrderByCartId(String cartId);

    CartOrder getCartOrderById(String id);

    CartOrder addCartOrder(CartOrder cartOrder);

    String deleteCartOrder(String cartOrderId);

    CartOrder updateCartOrder(CartOrder cartOrder);

    String deleteAllByCartId(String cartId);
}
