import java.util.List;


public class Cart {
    private Long cartId;
    private Long userId;
    private List<Long> cartOrder;

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Long> getCartOrder() {
        return cartOrder;
    }

    public void setCartOrder(List<Long> cartOrder) {
        this.cartOrder = cartOrder;
    }

    public Cart(Long cartId, Long userId, Long cartOrder) {
        this.cartId = cartId;
        this.userId = userId;
        this.cartOrder = cartOrder;



    }



}