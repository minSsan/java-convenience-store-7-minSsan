package store.domain;

import store.domain.vo.Product;
import store.domain.vo.Quantity;

public record ProductInfo(Product product, Quantity quantity, Promotion promotion) {
    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(String.format("%s %,d원 %s",
                product.name().value(),
                product.price().value(),
                quantity.toString()
        ));
        if (promotion == null) {
            return stringBuffer.toString();
        }
        return stringBuffer.append(String.format(" %s", promotion.name().value())).toString();
    }
}
