package store.service.strategy;

import store.domain.vo.Inventory;
import store.domain.vo.Order;
import store.domain.Promotion;
import store.service.dto.PromotionCommandResponse;

/**
 * 기존의 주문 내역을 변경하지 않고 프로모션을 적용하는 전략
 */
public class ImmutableOrderPromotionStrategy implements PromotionStrategy {
    @Override
    public PromotionCommandResponse apply(Order order, Inventory inventory, Promotion promotion) {
        return null;
    }
}