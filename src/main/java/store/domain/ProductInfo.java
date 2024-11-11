package store.domain;

import store.domain.vo.Product;
import store.domain.vo.Quantity;

public record ProductInfo(Product product, Quantity quantity, Promotion promotion) {
    public String name() {
        return product.name().value();
    }

    public int price() {
        return product.price().value();
    }
}
