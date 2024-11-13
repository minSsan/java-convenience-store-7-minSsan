package store.view.dto.product;

import store.domain.ProductInfo;

import java.util.List;

public class ProductViewRequest {
    public final List<ProductInfoDto> productInfoDtos;

    public ProductViewRequest(List<ProductInfo> productInfos) {
        productInfoDtos = productInfos.stream()
                .map(ProductInfoDto::new)
                .toList();
    }
}
