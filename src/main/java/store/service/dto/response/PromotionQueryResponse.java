package store.service.dto.response;

import store.domain.vo.Quantity;
import store.domain.vo.PromotionOption;

/**
 *
 * @param option 프로모션 적용 옵션
 * @param giftQuantity 프로모션 증정 제품 수량
 * @param appliedQuantity 프로모션 구입 제품 수량
 */
public record PromotionQueryResponse(
        PromotionOption option,
        Quantity giftQuantity,
        Quantity appliedQuantity,
        Quantity notAppliedQuantity
) {
}
