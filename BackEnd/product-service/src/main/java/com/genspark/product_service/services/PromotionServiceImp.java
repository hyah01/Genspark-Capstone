package com.genspark.product_service.services;

import com.genspark.product_service.entities.Promotion;
import com.genspark.product_service.repositories.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PromotionServiceImp implements PromotionService{
    @Autowired
    private PromotionRepository promotionRepo;

    @Override
    public Promotion addPromotion(Promotion promotion) {
        return this.promotionRepo.save(promotion);
    }

    @Override
    public Promotion updatePromotion(Promotion promotion) {
        return this.promotionRepo.save(promotion);
    }

    @Override
    public List<Promotion> getAllPromotions() {
        return this.promotionRepo.findAll();
    }

    @Override
    public Optional<Promotion> getPromotionByID(String id) {
        return this.promotionRepo.findById(id);
    }

    @Override
    public Promotion deletePromotion(String id) {
        this.promotionRepo.deleteById(id);
        return null;
    }
}
