package store.repository.inventory;

import store.domain.vo.Inventory;
import store.domain.vo.Name;
import store.domain.vo.Quantity;

import java.util.HashMap;

public class InventoryRepositoryImpl implements InventoryRepository {
    private final HashMap<Name, Inventory> items = new HashMap<>();

    @Override
    public Inventory findByProductName(Name name) {
        Inventory inventory = items.get(name);
        if (inventory == null) {
            return new Inventory(Quantity.ZERO, Quantity.ZERO);
        }
        return inventory;
    }

    @Override
    public void save(Name name, Inventory inventory) {
        items.put(name, inventory);
    }
}
