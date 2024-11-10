package store.repository.product;

import store.domain.vo.Product;
import store.domain.vo.Name;

public interface ProductRepository {
    Product findByName(Name name);
}
