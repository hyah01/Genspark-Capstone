public class CartOrder {
    private Long id;
    private Long UserId;
    private Long ProductId;
    private int Quantity;

    public CartOrder(Long id, Long userId, Long productId, int quantity) {
        this.id = id;
        UserId = userId;
        ProductId = productId;
        Quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return UserId;
    }

    public void setUserId(Long userId) {
        UserId = userId;
    }

    public Long getProductId() {
        return ProductId;
    }

    public void setProductId(Long productId) {
        ProductId = productId;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }
}