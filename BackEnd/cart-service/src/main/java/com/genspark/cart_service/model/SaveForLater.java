package com.genspark.cart_service.model;

public class SaveForLater {
    private Long cartOrderId;
    private long userId;
    private long productId;
    public SaveForLater(Long cartOrderId, long userId, long productId) {
        this.cartOrderId = cartOrderId;
        this.userId = userId;
        this.productId = productId;
    }
    public Long getCartOrderId() {
        return cartOrderId;
    }

    public void setCartOrderId(Long cartOrderId) {
        this.cartOrderId = cartOrderId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }


}
