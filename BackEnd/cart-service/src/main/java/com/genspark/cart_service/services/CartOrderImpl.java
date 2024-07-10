package com.genspark.cart_service.services;

import com.genspark.cart_service.model.Cart;
import com.genspark.cart_service.model.CartOrder;
import com.genspark.cart_service.repository.CartOrderRepository;

import java.util.List;
import java.util.Optional;

public class CartOrderImpl implements CartOrderService{

    private CartOrderRepository repository;
    @Override
    public List<CartOrder> getAllCartOrder() {
        return repository.findAll();
    }

    @Override
    public List<CartOrder> getAllCartOrderByCartId(String cartId) {
        return repository.getAllCartOrderByCartId(cartId);
    }

    @Override
    public CartOrder getById(String id) {
        Optional<CartOrder> c = repository.findById(id);
        CartOrder cart = null;
        if (c.isPresent()){
            cart = c.get();

        }
        return cart;
    }

    @Override
    public CartOrder addCartOrder(CartOrder cartOrder) {
        return repository.save(cartOrder);
    }

    @Override
    public String deleteCartOrder(String cartOrderId) {
        repository.deleteById(cartOrderId);
        return "CartOrder Deleted";
    }

    @Override
    public CartOrder updateCartOrder(CartOrder cartOrder) {
        return repository.save((cartOrder));
    }
}
