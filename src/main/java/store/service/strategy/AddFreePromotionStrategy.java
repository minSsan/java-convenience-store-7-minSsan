package store.service.strategy;

import store.domain.vo.Inventory;
import store.domain.vo.Order;
import store.domain.Promotion;
import store.domain.vo.PromotionQueryResult;
import store.domain.vo.*;
import store.service.dto.response.PromotionCommandResponse;

/**
 * 적용 가능한 프로모션 제품을 주문 내역에 추가하는 전략
 */
public class AddFreePromotionStrategy implements PromotionStrategy {
    @Override
    public PromotionCommandResponse apply(Order order, Inventory inventory, Promotion promotion) {
        Quantity summed = order.quantity().sum(promotion.getQuantity());
        Order resultOrder = new Order(order.productName(), summed);
        PromotionQueryResult promotionQueryResult = promotion.getQueryResult(resultOrder.quantity());
        return new PromotionCommandResponse(resultOrder, promotionQueryResult.gifted());
    }
}
