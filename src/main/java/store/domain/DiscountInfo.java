package store.domain;

import store.domain.vo.Price;
import store.domain.vo.Product;
import store.domain.vo.Quantity;
import store.infrastructure.constant.Membership;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DiscountInfo {
    private final Map<Product, Quantity> promotion;
    private Integer membership;

    public DiscountInfo() {
        promotion = new HashMap<>();
        membership = 0;
    }

    public void reset() {
        promotion.clear();
        membership = 0;
    }

    public void addPromotionDiscount(Product product, Quantity quantity) {
        if (promotion.get(product) == null) {
            promotion.put(product, quantity);
            return;
        }
        promotion.put(product, promotion.get(product).sum(quantity));
    }

    public void setMembershipDiscount(int total) {
        this.membership = Math.min(total * Membership.DISCOUNT_RATE / 100, Membership.MAX);
    }

    public int getTotalPromotionDiscount() {
        int total = 0;
        for (Product product : promotion.keySet()) {
            Price price = product.price();
            Quantity count = promotion.get(product);
            total += price.multiply(count);
        }
        return total;
    }

    public Map<Product, Quantity> getPromotionResult() {
        return Collections.unmodifiableMap(promotion);
    }

    public int getMembershipDiscount() {
        return this.membership;
    }
}
