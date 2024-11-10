package store.validator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import store.domain.Promotion;
import store.domain.vo.*;
import store.repository.inventory.InventoryRepository;
import store.repository.product.ProductRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class OrderValidatorTest {
    private final int inventoryCount = 5;
    private final Name existProductName = new Name("이미있는거");
    private final Name notExistProductName = new Name("없는거");

    class FixedInventoryRepository implements InventoryRepository {
        @Override
        public Inventory findByProductName(Name name) {
            return new Inventory(new Quantity(inventoryCount), Quantity.ZERO);
        }

        @Override
        public void save(Name name, Inventory inventory) {
        }
    }

    class FixedProductRepository implements ProductRepository {
        @Override
        public Promotion findPromotionByName(Name name) {
            return null;
        }

        @Override
        public Product findByName(Name name) {
            if (name.equals(existProductName)) {
                return new Product(existProductName, new Price(1000));
            }
            return null;
        }

        @Override
        public void save(Product product, Promotion promotion) {

        }
    }

    private final OrderValidator orderValidator = new OrderValidator(
            new FixedProductRepository(),
            new FixedInventoryRepository()
    );

    @Nested
    class 정상_케이스 {
        @Test
        @DisplayName("재고 수량에 맞는 주문을 검증할 수 있다.")
        void 재고_수량_정상() {
            // given
            List<Order> orders = List.of(
                    new Order(existProductName, new Quantity(inventoryCount))
            );

            // when & then
            orderValidator.validate(orders);
        }
    }

    @Nested
    class 예외_케이스 {
        @Test
        @DisplayName("재고 수량을 초과한 주문은 입력할 수 없다.")
        void 재고_초과() {
            // given
            List<Order> orders = List.of(
                    new Order(existProductName, new Quantity(inventoryCount+1))
            );

            // when & then
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> {
                        orderValidator.validate(orders);
                    })
                    .withMessage("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
        }

        @Test
        @DisplayName("상품 목록에 존재하지 않는 제품은 주문할 수 없다.")
        void 존재하지_않는_상품() {
            // given
            List<Order> orders = List.of(
                    new Order(notExistProductName, new Quantity(inventoryCount))
            );

            // when & then
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> {
                        orderValidator.validate(orders);
                    })
                    .withMessage("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.");
        }
    }
}