package store.repository.product;

import store.domain.vo.Product;
import store.domain.vo.Name;
import store.domain.Promotion;

import java.util.List;

public interface ProductRepository {
    List<Product> findAll();
    Promotion findPromotionByName(Name name);
    Product findByName(Name name);
    void save(Product product, Promotion promotion);
}
