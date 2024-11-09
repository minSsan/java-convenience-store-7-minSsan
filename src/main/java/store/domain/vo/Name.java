package store.domain.vo;

public record Name(String value) {
    public static final int MAX_LEN = 10;

    public Name(String value) {
        validateName(value.trim());
        this.value = value.trim();
    }

    private void validateName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 상품명은 공백으로 설정할 수 없습니다.");
        }

        if (name.length() > MAX_LEN) {
            throw new IllegalArgumentException("[ERROR] 상품 명은 최대 10자까지 설정할 수 있습니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof String) {
            return o.equals(value);
        }
        if (o instanceof Name) {
            return ((Name) o).value().equals(value);
        }
        return false;
    }
}
