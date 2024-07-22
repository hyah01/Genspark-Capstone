package com.genspark.cart_service.services;

import com.genspark.cart_service.model.SaveForLater;

import java.util.List;


public interface SaveForLaterService {
    List<SaveForLater> getSaveForLaterItems();

    List<SaveForLater> getSFLByCartId(String cartId);

    SaveForLater getById(String id);

    SaveForLater addSFLList(SaveForLater sfl);

    String deleteFromSFLList(String id);

    SaveForLater updateSFLList(SaveForLater sfl);

    String deleteAllByCartId(String cartId);
}
