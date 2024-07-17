package com.genspark.cart_service.controller;

import com.genspark.cart_service.model.SaveForLater;
import com.genspark.cart_service.services.SaveForLaterService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String delete(@PathVariable String id) {
        return service.deleteFromSFLList(id);
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
    public SaveForLater getById(@PathVariable String id) {
        return service.getById(id);
    }

    @DeleteMapping("/{cartId}") // delete all item in SFL database using CartId, call when deleting user
    public String deleteAllByCartId(@PathVariable String cartId) {
        return service.deleteAllByCartId(cartId);
    }
}
