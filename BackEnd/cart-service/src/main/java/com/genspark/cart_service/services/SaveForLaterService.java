package com.genspark.cart_service.services;

import com.genspark.cart_service.dto.CartItemReqRes;
import com.genspark.cart_service.dto.SFLReqRes;
import com.genspark.cart_service.model.CartItem;
import com.genspark.cart_service.model.SaveForLaterItem;
import com.genspark.cart_service.model.SaveForLaterItems;

import java.util.List;


public interface SaveForLaterService {
    SFLReqRes addSFLItem();

    SFLReqRes getSFLItemById(String id);

    SFLReqRes addItem(String id, SaveForLaterItem cart);

    SFLReqRes updateItem(String id, SaveForLaterItem cart);

    SFLReqRes deleteAllItem(String id, SaveForLaterItem cart);
}
