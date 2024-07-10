package com.genspark.cart_service.services;

import com.genspark.cart_service.model.Cart;
import com.genspark.cart_service.repository.CartRepository;
import java.util.Optional;

public class CartServiceImpl implements CartService{

    private CartRepository repository;
    private CartOrderService service;

    @Override
    public Cart getCartByCartId(String cartId) {
        Optional<Cart> c = repository.findById(cartId);
        Cart cart = null;
        if (c.isPresent()){
            cart = c.get();
            cart.setCartOrder(service.getAllCartOrderByCartId(cartId));
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
