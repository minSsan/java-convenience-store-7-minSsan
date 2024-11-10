package store.infrastructure.constant;

import store.domain.vo.Price;
import store.domain.Promotion;
import store.domain.vo.Quantity;

public enum ExceptionMessage {
    WRONG_INTEGER_FORMAT("숫자 형식이 올바르지 않습니다. 다시 입력해 주세요."),
    WRONG_QUANTITY_RANGE(String.format("재고는 %,d ~ %,d 사이의 숫자만 설정할 수 있습니다.", Quantity.MIN, Quantity.MAX)),
    WRONG_PRICE_RANGE(String.format("상품 가격은 %,d원 ~ %,d원 사이의 숫자만 유효합니다.", Price.MIN, Price.MAX)),
    WRONG_PROMOTION_DATE("프로모션 기간이 올바르게 설정되지 않았습니다."),
    WRONG_PROMOTION_GET_COUNT(String.format("현재 프로모션의 추가 상품은 %,d개만 가능합니다.", Promotion.GET)),
    EXCEED_ORDER_COUNT("재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요."),
    WRONG_INPUT_FORMAT("올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요."),
    ;

    private final String message;

    private ExceptionMessage(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}
