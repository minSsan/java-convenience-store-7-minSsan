package store.domain.vo;

import store.infrastructure.constant.ExceptionMessage;
import store.infrastructure.exception.CustomException;

public record Quantity(int value) implements Comparable<Quantity> {
    public static final Quantity ZERO = new Quantity(0);
    public static final int MIN = 0;
    public static final int MAX = 1000;

    public Quantity {
        validateQuantityRange(value);
    }

    public static Quantity of(String input) {
        try {
            return new Quantity(Integer.parseInt(input));
        } catch (NumberFormatException e) {
            throw new CustomException(ExceptionMessage.WRONG_INTEGER_FORMAT.message());
        }
    }

    private void validateQuantityRange(int value) {
        if (value < MIN || value > MAX) {
            throw new CustomException(ExceptionMessage.WRONG_QUANTITY_RANGE.message());
        }
    }

    public Quantity subtract(Quantity quantity) {
        if (quantity.value > value) {
            throw new IllegalStateException("기존 수량보다 큰 값을 뺄 수 없습니다.");
        }
        return new Quantity(value - quantity.value());
    }

    public Quantity sum(Quantity other) {
        final int result = value + other.value;
        return new Quantity(result);
    }

    public static Quantity min(Quantity a, Quantity b) {
        if (a.value < b.value) {
            return a;
        }
        return b;
    }

    @Override
    public String toString() {
        if (value == 0) {
            return "재고 없음";
        }
        return String.format("%,d개", value);
    }

    public boolean isSameWith(Quantity other) {
        return value == other.value;
    }

    public boolean isSameWith(int other) {
        return value == other;
    }

    public boolean isSmallerThan(Quantity other) {
        return this.value < other.value;
    }

    public boolean isSmallerThan(int other) {
        return this.value < other;
    }

    public boolean isGreaterThan(Quantity other) {
        return value > other.value;
    }

    public boolean isGreaterThan(int other) {
        return value > other;
    }

    public Quantity multiply(Quantity other) {
        return new Quantity(value * other.value);
    }

    public Quantity multiply(int other) {
        return new Quantity(value * other);
    }

    public Quantity division(Quantity other) {
        return new Quantity(value / other.value);
    }

    @Override
    public int compareTo(Quantity other) {
        return this.value - other.value;
    }
}
