package store.repository.promotion;

import store.domain.vo.Name;
import store.domain.Promotion;

public interface PromotionRepository {
    Promotion findByName(Name name);
    void save(Promotion promotion);
}
