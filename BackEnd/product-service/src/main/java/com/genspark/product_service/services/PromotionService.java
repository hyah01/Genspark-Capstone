package com.genspark.product_service.services;

import com.genspark.product_service.entities.Promotion;

import java.util.List;
import java.util.Optional;

public interface PromotionService {
    Promotion addPromotion(Promotion promotion);
    Promotion updatePromotion(Promotion promotion);
    List<Promotion> getAllPromotions();
    Optional<Promotion> getPromotionByID(String id);
    Promotion deletePromotion(String id);
}
