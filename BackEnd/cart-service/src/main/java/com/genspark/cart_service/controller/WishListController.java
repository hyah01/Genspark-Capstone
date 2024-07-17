package com.genspark.cart_service.controller;

import com.genspark.cart_service.model.WishList;
import com.genspark.cart_service.services.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wishlist")
public class WishListController {
    @Autowired
    private WishListService service;

    @PostMapping
    public WishList add(@RequestBody WishList wishList) {
        return service.addToWishList(wishList);
    }

    @PutMapping
    public WishList update(@RequestBody WishList wishList) {
        WishList updated = service.getById(wishList.getId());
        return service.updateWishList(updated);
    }

    @DeleteMapping("/byId/{id}")
    public String delete(@PathVariable String id) {
        return service.deleteFromWishList(id);
    }

    @GetMapping
    public List<WishList> findAll() {
        return service.getAllWishList();
    }

    @GetMapping("/{cartId}")
    public List<WishList> getByCartId(@PathVariable String cartId) {
        return service.getWishListByCartId(cartId);
    }

    @GetMapping("/byId/{id}")
    public WishList getById(@PathVariable String id) {
        return service.getById(id);
    }

    @DeleteMapping("/{cartId}")
    public String deleteAllByCartId(@PathVariable String cartId) {
        return service.deleteAllByCartId(cartId);
    }
}
