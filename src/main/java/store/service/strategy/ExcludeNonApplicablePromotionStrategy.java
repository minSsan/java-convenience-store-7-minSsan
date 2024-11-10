package store.service.strategy;

import store.domain.vo.Inventory;
import store.domain.vo.Order;
import store.domain.Promotion;
import store.service.dto.PromotionCommandResponse;

/**
 * 프로모션 적용 불가능 제품을 주문에서 제외하는 전략
 */
public class ExcludeNonApplicablePromotionStrategy implements PromotionStrategy {
    @Override
    public PromotionCommandResponse apply(Order order, Inventory inventory, Promotion promotion) {
        return null;
    }
}
