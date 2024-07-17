package com.genspark.cart_service.services;

import com.genspark.cart_service.model.Cart;
import com.genspark.cart_service.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartServiceImpl implements CartService{

    @Autowired
    private CartRepository repository;

    @Autowired
    private CartOrderService service;

    @Override // Return Null if item of id cannot be found
    public Cart getCartByCartId(String cartId) {
        Optional<Cart> c = repository.findById(cartId);
        Cart cart = null;
        if (c.isPresent()){
            cart = c.get();
            cart.setCartOrder(service.getAllCartOrderByCartId(cartId)); // setCartOrder into cart when called on
        }
        return cart;
    }

    @Override
    public Cart addCart(Cart cart) {
        return repository.save(cart);
    }

    @Override
    public String deleteCart(String cartId) {
        repository.deleteById(cartId);
        return "Deleted Cart";
    }


}
