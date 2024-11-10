package store.service.strategy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.Promotion;
import store.domain.vo.Inventory;
import store.domain.vo.Name;
import store.domain.vo.Order;
import store.domain.vo.Quantity;
import store.service.dto.PromotionCommandResponse;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class AddFreePromotionStrategyTest {
    private final AddFreePromotionStrategy addFreePromotionStrategy = new AddFreePromotionStrategy();

    @Test
    @DisplayName("추가 가능한 프로모션 제품이 있는 경우, 이를 추가하고 진행한다.")
    void 추가_가능_제품_추가() {
        // given
        Order order = new Order(
                new Name("제품명"),
                new Quantity(5)
        );
        Inventory inventory = new Inventory(
                new Quantity(6),
                new Quantity(0)
        );
        Promotion promotion = new Promotion(
                new Name("테스트"),
                new Quantity(1),
                new Quantity(1),
                LocalDate.now(),
                LocalDate.now().plusDays(1)
        );

        // when
        PromotionCommandResponse response = addFreePromotionStrategy.apply(order, inventory, promotion);

        // then
        assertThat(response.giftQuantity().value()).isEqualTo(3);
        assertThat(response.order().quantity().value())
                .isEqualTo(order.quantity().value() + 1);
    }
}