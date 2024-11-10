package store.repository.product;

import store.domain.vo.Product;
import store.domain.vo.Name;
import store.domain.Promotion;

public interface ProductRepository {
    Promotion findPromotionByName(Name name);
    Product findByName(Name name);
    void save(Product product, Promotion promotion);
}
