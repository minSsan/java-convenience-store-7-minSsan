package store.infrastructure.constant;

import store.domain.vo.Name;
import store.domain.vo.Price;
import store.domain.vo.Quantity;

public class ValueLength {
    public static final int PRICE_MAX = String.valueOf(Price.MAX).length();
    public static final int NAME_MAX = Name.MAX_LEN;
    public static final int QUANTITY_MAX = String.valueOf(Quantity.MAX).length();
}
