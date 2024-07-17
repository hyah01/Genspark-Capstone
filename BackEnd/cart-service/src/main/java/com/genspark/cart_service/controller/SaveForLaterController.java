package com.genspark.cart_service.controller;

import com.genspark.cart_service.model.SaveForLater;
import com.genspark.cart_service.model.WishList;
import com.genspark.cart_service.services.SaveForLaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/saveforlater")
public class SaveForLaterController {
    @Autowired
    private SaveForLaterService service;

    @PostMapping // Add item to SFL database
    public SaveForLater add(@RequestBody SaveForLater sfl) {
        return service.addSFLList(sfl);
    }

    @PutMapping // Update the item in SFL database
    public SaveForLater update(@RequestBody SaveForLater sfl) {
        SaveForLater updated = service.getById(sfl.getId());
        return service.updateSFLList(updated);
    }

    @DeleteMapping("/byId/{id}") // delete item in SFL database using ID
    public ResponseEntity<String> delete(@PathVariable String id) {
        String string = service.deleteFromSFLList(id);
        if (string == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(string);
        }
    }

    @GetMapping // get all item in SFL database
    public List<SaveForLater> findAll() {
        return service.getSaveForLaterItems();
    }

    @GetMapping("/{cartId}") // get all item in SFL database using CartId
    public List<SaveForLater> getByCartId(@PathVariable String cartId) {
        return service.getSFLByCartId(cartId);
    }

    @GetMapping("/byId/{id}")
    public ResponseEntity<SaveForLater> getById(@PathVariable String id) {
        SaveForLater wishList = service.getById(id);
        if (wishList == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(wishList);
        }
    }

    @DeleteMapping("/{cartId}") // delete all item in SFL database using CartId, call when deleting user
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
