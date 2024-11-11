package store.service.dto.request;

import store.domain.vo.Order;
import store.service.strategy.PromotionStrategy;

public record ApplyPromotionRequest(
        Order order,
        PromotionStrategy strategy
) {
}
