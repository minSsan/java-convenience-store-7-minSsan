package store.view.dto.order;

import store.domain.vo.Product;
import store.domain.vo.Quantity;

import java.util.List;
import java.util.Map;

public class ReceiptOrderViewRequest {
    public final List<ReceiptOrderDto> receiptOrderDtos;

    public ReceiptOrderViewRequest(Map<Product, Quantity> orders) {
        this.receiptOrderDtos = orders.keySet().stream()
                .map(product -> new ReceiptOrderDto(product, orders.get(product)))
                .toList();
    }
}
