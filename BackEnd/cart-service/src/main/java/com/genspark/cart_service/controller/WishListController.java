package com.genspark.cart_service.controller;

import com.genspark.cart_service.model.WishList;
import com.genspark.cart_service.services.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> delete(@PathVariable String id) {
        String string = service.deleteFromWishList(id);
        if (string == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(string);
        }
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
    public ResponseEntity<WishList> getById(@PathVariable String id) {
        WishList wishList = service.getById(id);
        if (wishList == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(wishList);
        }
    }

    @DeleteMapping("/{cartId}") // Deletes all wishlist items associated with a specific cart ID
    public ResponseEntity<String> deleteAllByCartId(@PathVariable String cartId) {
        String string = service.deleteAllByCartId(cartId);
        if (string == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(string);
        }
    }

    // Exception handling for this controller
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + ex.getMessage());
    }
}
