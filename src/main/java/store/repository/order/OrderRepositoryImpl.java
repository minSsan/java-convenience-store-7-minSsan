package store.repository.order;

import store.domain.vo.Product;
import store.domain.vo.Quantity;

import java.util.HashMap;

public class OrderRepositoryImpl implements OrderRepository {
    private final HashMap<Product, Quantity> items = new HashMap<>();

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
