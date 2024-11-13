package store.repository.promotion;

import store.domain.vo.Name;
import store.domain.Promotion;

import java.util.HashMap;

public class PromotionRepositoryImpl implements PromotionRepository {
    private final HashMap<Name, Promotion> items = new HashMap<>();

    @Override
    public Promotion findByName(Name name) {
        return items.get(name);
    }

    @Override
    public void save(Promotion promotion) {
        items.put(promotion.name(), promotion);
    }
}
