package store.service.strategy;

import store.domain.vo.Inventory;
import store.domain.vo.Order;
import store.domain.Promotion;
import store.domain.vo.PromotionQueryResult;
import store.domain.vo.*;
import store.service.dto.response.PromotionCommandResponse;

/**
 * 프로모션 적용 불가능 제품을 주문에서 제외하는 전략
 */
public class ExcludeNonApplicablePromotionStrategy implements PromotionStrategy {
    @Override
    public PromotionCommandResponse apply(Order order, Inventory inventory, Promotion promotion) {
        Quantity quantity = Quantity.min(order.quantity(), inventory.promotion());

        PromotionQueryResult query = promotion.getQueryResult(quantity);
        Quantity notAppliedQuantity = order.quantity().subtract(query.applied());

        Order resultOrder = new Order(order.productName(), order.quantity().subtract(notAppliedQuantity));

        return new PromotionCommandResponse(resultOrder, query.gifted());
    }
}
