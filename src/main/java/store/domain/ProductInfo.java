package store.domain;

import store.domain.vo.Product;
import store.domain.vo.Quantity;

public record ProductInfo(Product product, Quantity quantity, Promotion promotion) {
}
