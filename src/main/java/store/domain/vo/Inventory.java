package store.domain.vo;

public record Inventory(Quantity promotion, Quantity normal) {
    public Inventory subtractPromotion(Quantity quantity) {
        Quantity subtracted = promotion.subtract(quantity);
        return new Inventory(subtracted, normal);
    }

    public Inventory subtractNormal(Quantity quantity) {
        Quantity subtracted = normal.subtract(quantity);
        return new Inventory(promotion, subtracted);
    }

    public Inventory sumPromotion(Quantity quantity) {
        Quantity summed = promotion.sum(quantity);
        return new Inventory(summed, normal);
    }

    public Inventory sumNormal(Quantity quantity) {
        Quantity summed = normal.sum(quantity);
        return new Inventory(promotion, summed);
    }

    public int getTotal() {
        return promotion.value() + normal.value();
    }
}
