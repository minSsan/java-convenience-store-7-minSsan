package store.service.dto.response;

import store.domain.vo.Order;
import store.domain.vo.Quantity;

public record PromotionCommandResponse(
        Order order,
        Quantity giftQuantity
) {
}
