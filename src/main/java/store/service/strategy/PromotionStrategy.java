package store.service.strategy;

import store.domain.vo.Inventory;
import store.domain.vo.Order;
import store.domain.Promotion;
import store.service.dto.response.PromotionCommandResponse;

public interface PromotionStrategy {
    PromotionCommandResponse apply(Order order, Inventory inventory, Promotion promotion);
}
