package com.genspark.cart_service.services;

import com.genspark.cart_service.dto.CartItemReqRes;
import com.genspark.cart_service.dto.SFLReqRes;
import com.genspark.cart_service.model.CartItems;
import com.genspark.cart_service.model.SaveForLaterItem;
import com.genspark.cart_service.model.SaveForLaterItems;
import com.genspark.cart_service.repository.SaveForLaterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SaveForLaterImpl implements SaveForLaterService {

    @Autowired
    private SaveForLaterRepository repository;

    @Override
    public SFLReqRes addSFLItem() {
        SFLReqRes reqRes = new SFLReqRes();
        try {
            // Create new Save For Later Items
            SaveForLaterItems SFLItems = new SaveForLaterItems();
            SFLItems.setItems(new HashMap<>()); // Initialize the map
            // Save the cart
            SaveForLaterItems result = repository.save(SFLItems);
            // If cart was created successfully
            if (result.getId() != null && !result.getId().isEmpty()) {
                reqRes.setSflItems(result);
                reqRes.setMessage("Successfully Created Save For Later Cart");
                reqRes.setStatusCode(200);
            } else {
                // If cart didn't create successfully
                reqRes.setStatusCode(500);
                reqRes.setMessage("Unsuccessfully Created Save For Later Cart");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error: " + e.getMessage());
        }
        return reqRes;
    }

    @Override
    public SFLReqRes getSFLItemById(String id) {
        SFLReqRes reqRes = new SFLReqRes();
        try {
            SaveForLaterItems sflItems = repository.findById(id).orElseThrow(() -> new RuntimeException("Save For Later Cart Not Found"));
            reqRes.setSflItems(sflItems);
            reqRes.setStatusCode(200);
            reqRes.setMessage("Save For Later Cart with id '" + id + "' found successfully");
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error Occurred: " + e.getMessage());
        }
        return reqRes;
    }

    @Override
    public SFLReqRes addItem(String id, SaveForLaterItem sflItem) {
        SFLReqRes reqRes = new SFLReqRes();
        try {
            // Fetch the Save For Later Cart
            SaveForLaterItems sflItems = getSFLItemById(id).getSflItems();

            // Ensure the map is initialized
            Map<String, SaveForLaterItem> items = sflItems.getItems();
            if (items == null) {
                items = new HashMap<>();
                sflItems.setItems(items);
            }

            // Add or update the item in the cart
            items.put(sflItem.getProductId(), sflItem);

            // Save the updated cart
            repository.save(sflItems);

            reqRes.setSflItems(sflItems);
            reqRes.setStatusCode(200);
            reqRes.setMessage("Successfully updated Save For Later cart");
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error Occurred: " + e.getMessage());
        }
        return reqRes;
    }

    @Override
    public SFLReqRes updateItem(String id, SaveForLaterItem sflItem) {
        SFLReqRes reqRes = new SFLReqRes();
        try {
            // Fetch the Save For Later Cart
            SaveForLaterItems sflItems = getSFLItemById(id).getSflItems();

            // Ensure the map is initialized
            Map<String, SaveForLaterItem> items = sflItems.getItems();
            if (items == null) {
                items = new HashMap<>();
                sflItems.setItems(items);
            }

            // Update or remove the item based on quantity
            if (sflItem.getQuantity() <= 0) {
                items.remove(sflItem.getProductId());
            } else {
                items.put(sflItem.getProductId(), sflItem);
            }

            // Save the updated cart
            repository.save(sflItems);
            reqRes.setSflItems(sflItems);
            reqRes.setStatusCode(200);
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error Occurred: " + e.getMessage());
        }
        return reqRes;
    }

    @Override
    public SFLReqRes deleteAllItem(String id, SaveForLaterItem sflItem) {
        SFLReqRes reqRes = new SFLReqRes();
        try {
            // Fetch the Save For Later Cart
            SaveForLaterItems sflItems = getSFLItemById(id).getSflItems();

            // Clear the cart
            Map<String, SaveForLaterItem> items = sflItems.getItems();
            if (items != null) {
                items.clear();
            }

            // Save the updated cart
            repository.save(sflItems);
            reqRes.setSflItems(sflItems);
            reqRes.setStatusCode(200);
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error Occurred: " + e.getMessage());
        }
        return reqRes;
    }
}
