package store.domain;

import store.domain.vo.*;

import java.time.LocalDate;

public record Promotion(Name name, Quantity buyQuantity, Quantity getQuantity, LocalDate start, LocalDate end) {
    public static final int GET = 1;

    public static Promotion of(String name, int buy, int get, LocalDate start, LocalDate end) {
        return new Promotion(new Name(name), new Quantity(buy), new Quantity(get), start, end);
    }

    public boolean isApplicable() {
        return false;
    }

    public PromotionQueryResult getQueryResult(Quantity quantity) {
        return null;
    }
}
