package store.validator;

import store.domain.vo.Inventory;
import store.domain.vo.Order;
import store.domain.vo.Product;
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
        validateProductName(orders);
        validateExceed(orders);
    }

    private void validateExceed(List<Order> orders) {
        orders.forEach(order -> {
            Inventory inventory = inventoryRepository.findByProductName(order.productName());
            if (order.quantity().value() > inventory.getTotal()) {
                throw new IllegalArgumentException("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
            }
        });
    }

    private void validateProductName(List<Order> orders) {
        orders.forEach(order -> {
            Product product = productRepository.findByName(order.productName());
            if (product == null) {
                throw new IllegalArgumentException("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.");
            }
        });
    }
}
