package store.repository.product;

import store.domain.vo.Product;
import store.domain.vo.Name;
import store.domain.Promotion;

import java.util.HashMap;

public class ProductRepositoryImpl implements ProductRepository {
    private final HashMap<Product, Promotion> items = new HashMap<>();

    @Override
    public Promotion findPromotionByName(Name name) {
        Product product = this.findByName(name);
        if (product == null) {
            return null;
        }
        return items.get(product);
    }

    @Override
    public Product findByName(Name name) {
        return items.keySet().stream()
                .filter(product -> product.name().equals(name))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void save(Product product, Promotion promotion) {
        items.put(product, promotion);
    }
}
