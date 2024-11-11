package store.service.strategy;

import store.domain.vo.Inventory;
import store.domain.vo.Order;
import store.domain.Promotion;
import store.service.dto.response.PromotionCommandResponse;

public interface PromotionStrategy {
    /**
     *
     * @param order 주문 정보
     * @return 프로모션 혜택 적용 수량
     */
    PromotionCommandResponse apply(Order order, Inventory inventory, Promotion promotion);
}
