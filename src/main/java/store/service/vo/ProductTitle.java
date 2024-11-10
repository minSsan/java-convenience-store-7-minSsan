package store.service.vo;

import java.util.Arrays;

public enum ProductTitle {
    NAME("name"),
    PRICE("price"),
    QUANTITY("quantity"),
    PROMOTION("promotion"),
    ;

    public final String text;

    private ProductTitle(String text) {
        this.text = text;
    }

    public static ProductTitle from(String input) {
        return Arrays.stream(ProductTitle.values())
                .filter(value -> input.equalsIgnoreCase(value.text))
                .findFirst()
                .orElse(null);
    }
}
