package store.service;

import store.repository.inventory.InventoryRepository;
import store.repository.product.ProductRepository;
import store.repository.promotion.PromotionRepository;

/**
 * 가게 제품, 프로모션, 재고 정보를 파일 입력으로 받아서 저장하는 서비스
 */
public class StoreFileInputService {
    private static final String PROMOTION_PATH = "src/main/resources/promotions.md";
    private static final String PRODUCT_PATH = "src/main/resources/products.md";

    private final ProductRepository productRepository;
    private final PromotionRepository promotionRepository;
    private final InventoryRepository inventoryRepository;

    public StoreFileInputService(
            ProductRepository productRepository,
            PromotionRepository promotionRepository,
            InventoryRepository inventoryRepository
    ) {
        this.productRepository = productRepository;
        this.promotionRepository = promotionRepository;
        this.inventoryRepository = inventoryRepository;
    }

    public void loadAndSave() {
    }
}
