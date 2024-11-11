package store.view.dto.order;

import store.domain.vo.Product;
import store.domain.vo.Quantity;

public record ReceiptOrderDto(String name, int quantity, int price) {
    public ReceiptOrderDto(Product product, Quantity quantity) {
        this(product.name().value(), quantity.value(), product.price().value());
    }
}
