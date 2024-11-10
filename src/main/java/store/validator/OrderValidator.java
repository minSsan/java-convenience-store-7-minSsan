package store.validator;

import store.domain.vo.Order;
import store.repository.inventory.InventoryRepository;
import store.repository.product.ProductRepository;

import java.util.List;

/**
 * 사용자의 주문 내역을 검증한다.
 */
public class OrderValidator {
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    public OrderValidator(ProductRepository productRepository, InventoryRepository inventoryRepository) {
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
    }

    public void validate(List<Order> orders) {
    }
}
