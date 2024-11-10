package store.service.strategy;

import store.domain.vo.Inventory;
import store.domain.vo.Order;
import store.domain.Promotion;
import store.service.dto.PromotionCommandResponse;

/**
 * 적용 가능한 프로모션 제품을 주문 내역에 추가하는 전략
 */
public class AddFreePromotionStrategy implements PromotionStrategy {
    @Override
    public PromotionCommandResponse apply(Order order, Inventory inventory, Promotion promotion) {
        return null;
    }
}
