package store.view.dto.product;

import store.domain.ProductInfo;
import store.domain.Promotion;

public record ProductInfoDto(String name, int price, String quantity, Promotion promotion) {
    public ProductInfoDto(ProductInfo productInfo) {
        this(
                productInfo.product().name().value(),
                productInfo.product().price().value(),
                productInfo.quantity().toString(),
                productInfo.promotion()
        );
    }
}