package com.genspark.cart_service.services;

import com.genspark.cart_service.model.CartOrder;
import com.genspark.cart_service.model.WishList;

import java.util.List;

public interface WishListService {

    List<WishList> getAllWishList();

    List<WishList> getWishListByCartId(String cartId);

    WishList getById(String id);

    WishList addToWishList(WishList wishList);

    String deleteFromWishList(String id);

    WishList updateWishList(WishList wishList);

    String deleteAllByCartId(String cartId);
}
