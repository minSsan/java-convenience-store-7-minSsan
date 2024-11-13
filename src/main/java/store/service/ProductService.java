package store.service;

import store.domain.ProductInfo;
import store.domain.vo.Inventory;
import store.domain.Promotion;
import store.domain.vo.*;
import store.repository.inventory.InventoryRepository;
import store.repository.order.OrderRepository;
import store.repository.product.ProductRepository;

import java.util.ArrayList;
import java.util.List;

public class ProductService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    public ProductService(
            OrderRepository orderRepository,
            ProductRepository productRepository,
            InventoryRepository inventoryRepository
    ) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
    }

    public List<ProductInfo> getAllProductInfo() {
        List<ProductInfo> result = new ArrayList<>();
        productRepository.findAll().forEach(product -> {
            Inventory inventory = inventoryRepository.findByProductName(product.name());
            Promotion promotion = productRepository.findPromotionByName(product.name());
            if (promotion != null) {
                result.add(new ProductInfo(product, inventory.promotion(), promotion));
            }
            result.add(new ProductInfo(product, inventory.normal(), null));
        });
        return result;
    }

    public void applyReceiptInventory() {
        productRepository.findAll().forEach(product -> {
            Quantity quantity = orderRepository.findQuantityByProduct(product);
            Promotion promotion = productRepository.findPromotionByName(product.name());
            Inventory inventory = inventoryRepository.findByProductName(product.name());
            if (promotion == null || !promotion.isApplicable()) {
                updateNormalInventory(product, quantity, inventory);
                return;
            }
            updatePromotionInventory(product, quantity, inventory);
        });
    }

    private void updateNormalInventory(Product product, Quantity quantity, Inventory inventory) {
        Inventory updated = inventory.subtractNormal(quantity);
        inventoryRepository.save(product.name(), updated);
    }

    private void updatePromotionInventory(Product product, Quantity quantity, Inventory inventory) {
        Quantity promotionQuantity = inventory.promotion();
        if (quantity.isGreaterThan(promotionQuantity)) {
            Quantity usedNormalQuantity = quantity.subtract(inventory.promotion());
            Quantity remainNormalQuantity = inventory.normal().subtract(usedNormalQuantity);
            inventoryRepository.save(product.name(), new Inventory(Quantity.ZERO, remainNormalQuantity));
            return;
        }
        Inventory updated = inventory.subtractPromotion(quantity);
        inventoryRepository.save(product.name(), updated);
    }
}
