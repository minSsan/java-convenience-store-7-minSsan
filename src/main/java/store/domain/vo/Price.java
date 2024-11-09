package store.domain.vo;

public record Price(int value) {
    public static final int MIN = 10;
    public static final int MAX = 1_000_000;

    public Price {
        validatePrice(value);
    }

    private void validatePrice(int value) {
        if (value < MIN || value > MAX) {
            throw new IllegalArgumentException("[ERROR] 상품 가격은 10 ~ 1,000,000 사이 숫자만 가능합니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Integer) {
            return (Integer) o == value;
        }
        if (o instanceof Price) {
            return ((Price) o).value() == value;
        }
        return false;
    }
}
