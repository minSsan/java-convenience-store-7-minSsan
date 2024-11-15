package store.domain.vo;

import store.infrastructure.constant.ExceptionMessage;
import store.infrastructure.exception.CustomException;

public record Price(int value) implements Comparable<Price> {
    public static final int MIN = 10;
    public static final int MAX = 1_000_000;

    public Price {
        validatePrice(value);
    }

    public static Price from(String input) {
        try {
            return new Price(Integer.parseInt(input.trim()));
        } catch (NumberFormatException e) {
            throw new CustomException(ExceptionMessage.WRONG_INTEGER_FORMAT.message());
        }
    }

    private void validatePrice(int value) {
        if (value < MIN || value > MAX) {
            throw new CustomException(ExceptionMessage.WRONG_PRICE_RANGE.message());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Integer) {
            return (Integer) o == value;
        }
        if (o instanceof Price) {
            return ((Price) o).value == value;
        }
        return false;
    }

    @Override
    public int compareTo(Price other) {
        return value - other.value;
    }

    public int multiply(Quantity quantity) {
        return quantity.value() * value;
    }

    public int multiply(int other) {
        if (other <= 0) {
            throw new IllegalStateException("0 이하의 숫자는 가격에 곱할 수 없습니다.");
        }
        return other * value;
    }
}
