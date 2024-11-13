package store.repository.inventory;

import store.domain.vo.Inventory;
import store.domain.vo.Name;

public interface InventoryRepository {
    Inventory findByProductName(Name name);
    void save(Name name, Inventory inventory);
}
