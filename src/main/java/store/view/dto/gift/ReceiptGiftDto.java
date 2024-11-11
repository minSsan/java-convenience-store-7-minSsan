package store.view.dto.gift;

import store.domain.vo.Product;
import store.domain.vo.Quantity;

public record ReceiptGiftDto(String name, int quantity) {
    public ReceiptGiftDto(Product product, Quantity quantity) {
        this(product.name().value(), quantity.value());
    }
}
