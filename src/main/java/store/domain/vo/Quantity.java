package store.domain.vo;

import store.infrastructure.constant.ExceptionMessage;
import store.infrastructure.exception.CustomException;

public record Quantity(int value) {
    public static final Quantity ZERO = new Quantity(0);
    public static final int MIN = 0;
    public static final int MAX = 1000;

    public Quantity {
        validateQuantityRange(value);
    }

    public static Quantity from(String input) {
    }

    private void validateQuantityRange(int value) {
        if (value < MIN || value > MAX) {
            throw new CustomException(ExceptionMessage.WRONG_QUANTITY_RANGE.message());
        }
    }

    public Quantity subtract(Quantity quantity) {
    }

    public Quantity sum(Quantity other) {
    }

    @Override
    public String toString() {
        if (value == 0) {
            return "재고 없음";
        }
        return String.format("%,d개", value);
    }
}
