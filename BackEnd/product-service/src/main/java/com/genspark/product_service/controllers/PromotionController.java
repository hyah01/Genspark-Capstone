package com.genspark.product_service.controllers;

import com.genspark.product_service.entities.Product;
import com.genspark.product_service.entities.Promotion;
import com.genspark.product_service.services.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/promotion")
public class PromotionController {
    @Autowired
    private PromotionService promotionServ;

    @GetMapping("/all")
    public List<Promotion> getAllPromotions() {
        return this.promotionServ.getAllPromotions();
    }

    @GetMapping("/one/{id}")
    public Optional<Promotion> getOnePromotion(@PathVariable String id) {
        return this.promotionServ.getPromotionByID(id);
    }

    @PostMapping("/add")
    public Promotion addPromotion(@RequestBody Promotion promotion) {
        return this.promotionServ.addPromotion(promotion);
    }

    @PutMapping("/update")
    public Promotion updatePromotion(@RequestBody Promotion promotion) {
        return this.promotionServ.updatePromotion(promotion);
    }

    @DeleteMapping("/delete/{id}")
    public Promotion deletePromotion(@PathVariable String id) {
        return this.promotionServ.deletePromotion(id);
    }

}
