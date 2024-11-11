package store.view.dto.gift;

import store.domain.vo.Product;
import store.domain.vo.Quantity;

import java.util.List;
import java.util.Map;

public class ReceiptGiftViewRequest {
    public final List<ReceiptGiftDto> receiptGiftDtos;

    public ReceiptGiftViewRequest(Map<Product, Quantity> promotionResult) {
        this.receiptGiftDtos = promotionResult.keySet().stream()
                .map(product -> new ReceiptGiftDto(product, promotionResult.get(product)))
                .toList();
    }
}
