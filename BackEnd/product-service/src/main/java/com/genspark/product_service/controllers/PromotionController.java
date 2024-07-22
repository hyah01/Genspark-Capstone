package com.genspark.product_service.controllers;

import com.genspark.product_service.entities.Product;
import com.genspark.product_service.entities.Promotion;
import com.genspark.product_service.services.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/promotion")
public class PromotionController {
    @Autowired
    private PromotionService promotionServ;

    // Retrieves all promotions.
    @GetMapping("/all")
    public ResponseEntity<List<Promotion>> getAllPromotions() {
        List<Promotion> promotions = promotionServ.getAllPromotions();
        return new ResponseEntity<>(promotions, HttpStatus.OK);
    }

    // Retrieves a single promotion by ID.
    @GetMapping("/one/{id}")
    public ResponseEntity<Optional<Promotion>> getOnePromotion(@PathVariable String id) {
        Optional<Promotion> promotion = promotionServ.getPromotionByID(id);
        if (promotion.isPresent()) {
            return new ResponseEntity<>(promotion, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    // Adds a new promotion.
    @PostMapping("/add")
    public ResponseEntity<Promotion> addPromotion(@RequestBody Promotion promotion) {
        Promotion addedPromotion = promotionServ.addPromotion(promotion);
        return new ResponseEntity<>(addedPromotion, HttpStatus.CREATED);
    }


    // Updates an existing promotion.
    @PutMapping("/update")
    public ResponseEntity<Promotion> updatePromotion(@RequestBody Promotion promotion) {
        Promotion updatedPromotion = promotionServ.updatePromotion(promotion);
        if (updatedPromotion != null) {
            return new ResponseEntity<>(updatedPromotion, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Deletes a promotion by ID.
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePromotion(@PathVariable String id) {
        boolean isDeleted = promotionServ.deletePromotion(id);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
