package com.genspark.cart_service.services;

import com.genspark.cart_service.model.SaveForLater;
import com.genspark.cart_service.repository.SaveForLaterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaveForLaterImpl implements SaveForLaterService{
    @Autowired
    private SaveForLaterRepository repository;

    @Override
    public List<SaveForLater> getSaveForLaterItems() {
        return repository.findAll();
    }

    @Override
    public List<SaveForLater> getSFLByCartId(String cartId) {
        return repository.getSFLByCartId(cartId);
    }

    @Override
    public SaveForLater getById(String id) {
        return repository.findById(id).orElse(null); // Return Null if item of id cannot be found
    }

    @Override
    public SaveForLater addSFLList(SaveForLater sfl) {
        return repository.save(sfl);
    }

    @Override
    public String deleteFromSFLList(String id) {
        repository.deleteById(id);
        return "SaveForLater item with ID " + id + " has been deleted.";
    }

    @Override
    public SaveForLater updateSFLList(SaveForLater sfl) {
        return repository.save(sfl);
    }

    @Override
    public String deleteAllByCartId(String cartId) {
        repository.deleteAllByCartId(cartId);
        return "Deleted all SaveForLater items with Cart ID " + cartId;
    }
}
