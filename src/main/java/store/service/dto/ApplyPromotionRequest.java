package store.service.dto;

import store.domain.vo.Order;
import store.service.strategy.PromotionStrategy;

public record ApplyPromotionRequest(
        Order order,
        PromotionStrategy strategy
) {
}
