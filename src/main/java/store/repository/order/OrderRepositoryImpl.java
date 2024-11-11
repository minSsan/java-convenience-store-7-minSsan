package store.repository.order;

import store.domain.vo.Product;
import store.domain.vo.Quantity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class OrderRepositoryImpl implements OrderRepository {
    private final Map<Product, Quantity> items = new HashMap<>();

    @Override
    public Map<Product, Quantity> findAll() {
        return Collections.unmodifiableMap(items);
    }

    @Override
    public Quantity findQuantityByProduct(Product product) {
        Quantity quantity = items.get(product);
        if (quantity == null) {
            return Quantity.ZERO;
        }
        return quantity;
    }

    @Override
    public void save(Product product, Quantity quantity) {
        items.put(product, quantity);
    }

    @Override
    public void deleteAll() {
        items.clear();
    }
}
