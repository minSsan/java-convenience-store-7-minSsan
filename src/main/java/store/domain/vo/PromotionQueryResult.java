package store.domain.vo;

/**
 *
 * @param gifted 증정 수량
 * @param applied 적용 프로모션 제품 수
 * @param nonApplied 미적용 프로모션 제품 수
 */
public record PromotionQueryResult(
        Quantity gifted,
        Quantity applied,
        Quantity nonApplied
) {
}
