package com.genspark.cart_service.controller;

import com.genspark.cart_service.model.SaveForLater;
import com.genspark.cart_service.model.WishList;
import com.genspark.cart_service.services.SaveForLaterService;
import com.genspark.cart_service.services.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/saveforlater")
public class SaveForLaterController {
    @Autowired
    private SaveForLaterService service;

    @PostMapping
    public SaveForLater add(@RequestBody SaveForLater sfl) {
        return service.addSFLList(sfl);
    }

    @PutMapping
    public SaveForLater update(@RequestBody SaveForLater sfl) {
        SaveForLater updated = service.getById(sfl.getId());
        return service.updateSFLList(updated);
    }

    @DeleteMapping("/byId/{id}")
    public String delete(@PathVariable String id) {
        return service.deleteFromSFLList(id);
    }

    @GetMapping
    public List<SaveForLater> findAll() {
        return service.getSaveForLaterItems();
    }

    @GetMapping("/{cartId}")
    public List<SaveForLater> getByCartId(@PathVariable String cartId) {
        return service.getSFLByCartId(cartId);
    }

    @GetMapping("/byId/{id}")
    public SaveForLater getById(@PathVariable String id) {
        return service.getById(id);
    }

    @DeleteMapping("/{cartId}")
    public String deleteAllByCartId(@PathVariable String cartId) {
        return service.deleteAllByCartId(cartId);
    }
}
