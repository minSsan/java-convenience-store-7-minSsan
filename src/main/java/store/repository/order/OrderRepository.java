package store.repository.order;

import store.domain.vo.Product;
import store.domain.vo.Quantity;

import java.util.Map;

public interface OrderRepository {
    Map<Product, Quantity> findAll();
    Quantity findQuantityByProduct(Product product);
    void save(Product product, Quantity quantity);
    void deleteAll();
}
