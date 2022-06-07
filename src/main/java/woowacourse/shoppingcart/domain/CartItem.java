package woowacourse.shoppingcart.domain;

import woowacourse.shoppingcart.domain.product.Product;

public class CartItem {

    private final Long id;
    private final Product product;
    private final int quantity;

    public CartItem(Long id, Product product, int quantity) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return product.getName();
    }

    public int getPrice() {
        return product.getPrice();
    }

    public String getImageUrl() {
        return product.getImageUrl();
    }

    public int getQuantity() {
        return quantity;
    }
}
