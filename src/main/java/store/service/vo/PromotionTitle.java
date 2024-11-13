package store.service.vo;

import java.util.Arrays;

public enum PromotionTitle {
    NAME("name"),
    BUY("buy"),
    GET("get"),
    START_DATE("start_date"),
    END_DATE("end_date"),
    ;

    public final String text;

    private PromotionTitle(String text) {
        this.text = text;
    }

    public static PromotionTitle from(String input) {
        return Arrays.stream(PromotionTitle.values())
                .filter(value -> input.equalsIgnoreCase(value.text))
                .findFirst()
                .orElse(null);
    }
}