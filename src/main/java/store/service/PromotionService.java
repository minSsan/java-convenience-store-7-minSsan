package store.service;

import store.domain.vo.Inventory;
import store.domain.vo.Order;
import store.domain.Promotion;
import store.domain.vo.PromotionQueryResult;
import store.domain.vo.*;
import store.repository.inventory.InventoryRepository;
import store.repository.product.ProductRepository;
import store.service.dto.PromotionCommandResponse;
import store.service.dto.PromotionQueryResponse;
import store.domain.vo.PromotionOption;
import store.service.strategy.PromotionStrategy;

public class PromotionService {
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    public PromotionService(
            ProductRepository productRepository,
            InventoryRepository inventoryRepository
    ) {
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
    }

    public PromotionQueryResponse query(Order order) {
        if (!isExistApplicablePromotion(order.productName())) {
            return new PromotionQueryResponse(PromotionOption.NONE, Quantity.ZERO, Quantity.ZERO);
        }
        Promotion promotion = productRepository.findPromotionByName(order.productName());
        Inventory inventory = inventoryRepository.findByProductName(order.productName());

        Quantity applyQuantity = Quantity.min(order.quantity(), inventory.promotion());
        PromotionQueryResult result = promotion.getQueryResult(applyQuantity);
        PromotionOption promotionOption = promotion.getPromotionOption(applyQuantity, inventory);
        return new PromotionQueryResponse(promotionOption, result.gifted(), result.applied());
    }

    public PromotionCommandResponse command(Order order, PromotionStrategy promotionStrategy) {
        Promotion promotion = productRepository.findPromotionByName(order.productName());
        Inventory inventory = inventoryRepository.findByProductName(order.productName());
        return promotionStrategy.apply(order, inventory, promotion);
    }

    /**
     * 현재 제품에 적용 가능한 프로모션이 존재하는지
     * @param productName 제품명
     * @return
     */
    public boolean isExistApplicablePromotion(Name productName) {
        Promotion promotion = productRepository.findPromotionByName(productName);
        if (promotion == null) {
            return false;
        }
        return promotion.isApplicable();
    }
}
