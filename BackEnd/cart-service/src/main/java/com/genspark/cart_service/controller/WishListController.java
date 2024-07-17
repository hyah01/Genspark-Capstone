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

    @PostMapping // Adds a new item to the wishlist
    public WishList add(@RequestBody WishList wishList) {
        return service.addToWishList(wishList);
    }

    @PutMapping // Updates an existing item in the wishlist
    public WishList update(@RequestBody WishList wishList) {
        WishList updated = service.getById(wishList.getId());
        return service.updateWishList(updated);
    }

    @DeleteMapping("/byId/{id}") // Deletes a specific item from the wishlist by its ID
    public String delete(@PathVariable String id) {
        return service.deleteFromWishList(id);
    }

    @GetMapping // Retrieves all items in the wishlist
    public List<WishList> findAll() {
        return service.getAllWishList();
    }

    @GetMapping("/{cartId}") // Retrieves all wishlist items associated with a specific cart ID
    public List<WishList> getByCartId(@PathVariable String cartId) {
        return service.getWishListByCartId(cartId);
    }

    @GetMapping("/byId/{id}") // Retrieves a specific wishlist item by its ID
    public WishList getById(@PathVariable String id) {
        return service.getById(id);
    }

    @DeleteMapping("/{cartId}") // Deletes all wishlist items associated with a specific cart ID
    public String deleteAllByCartId(@PathVariable String cartId) {
        return service.deleteAllByCartId(cartId);
    }
}
