package com.genspark.cart_service.services;

import com.genspark.cart_service.model.Cart;
import com.genspark.cart_service.model.CartOrder;
import com.genspark.cart_service.repository.CartOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartOrderImpl implements CartOrderService{

    @Autowired
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
    public CartOrder getCartOrderById(String id) {
        return repository.findById(id).orElse(null); // Return Null if item of id cannot be found
    }

    @Override
    public CartOrder addCartOrder(CartOrder cartOrder) {
        return repository.save(cartOrder);
    }

    @Override
    public String deleteCartOrder(String cartOrderId) {
        if (repository.existsById(cartOrderId)) {
            repository.deleteById(cartOrderId);
            return "CartOrder Deleted";
        } else {
            return null;
        }
    }

    @Override
    public CartOrder updateCartOrder(CartOrder cartOrder) {
        return repository.save((cartOrder));
    }

    @Override
    public String deleteAllByCartId(String cartId){
        if (repository.existsById(cartId)) {
            repository.deleteAllByCartId(cartId);
            return "All order from cart " + cartId + " has been deleted";
        } else {
            return null;
        }
    }
}
