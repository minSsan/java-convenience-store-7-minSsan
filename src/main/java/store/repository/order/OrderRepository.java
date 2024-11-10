package store.repository.order;

import store.domain.vo.Product;
import store.domain.vo.Quantity;

public interface OrderRepository {
    Quantity findQuantityByProduct(Product product);
    void save(Product product, Quantity quantity);
    void deleteAll();
}
