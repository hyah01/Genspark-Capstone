package com.genspark.cart_service.services;

import com.genspark.cart_service.model.CartOrder;
import com.genspark.cart_service.model.WishList;
import com.genspark.cart_service.repository.WishListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WishListImpl implements WishListService{

    @Autowired
    private WishListRepository repository;
    @Override
    public List<WishList> getAllWishList() {
        return repository.findAll();
    }

    @Override
    public List<WishList> getWishListByCartId(String cartId) {
        return repository.getWishListByCartId(cartId);
    }

    @Override
    public WishList getById(String id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public WishList addToWishList(WishList wishList) {
        return repository.save(wishList);
    }

    @Override
    public String deleteFromWishList(String id) {
        repository.deleteById(id);
        return "Wishlist item with ID " + id + " has been deleted.";
    }

    @Override
    public WishList updateWishList(WishList wishList) {
        return repository.save(wishList);
    }

    @Override
    public String deleteAllByCartId(String cartId) {
        repository.deleteAllByCartId(cartId);
        return "Deleted All Wishlist Item with Cart ID " + cartId;
    }
}
