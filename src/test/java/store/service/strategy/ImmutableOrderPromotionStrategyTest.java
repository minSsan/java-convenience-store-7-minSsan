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

class ImmutableOrderPromotionStrategyTest {
    private final ImmutableOrderPromotionStrategy immutableOrderPromotionStrategy
            = new ImmutableOrderPromotionStrategy();

    @Test
    @DisplayName("추가 가능한 프로모션 제품이 있는 경우, 이를 추가하지 않고 진행한다.")
    void 추가_가능_제품_무시() {
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
        PromotionCommandResponse response = immutableOrderPromotionStrategy.apply(order, inventory, promotion);

        // then
        assertThat(response.giftQuantity().value()).isEqualTo(2);
        assertThat(response.order()).isEqualTo(order);
    }

    @Test
    @DisplayName("프로모션 미적용 제품이 있는 경우, 이를 제외하지 않고 진행한다.")
    void 프로모션_미적용_제품_구입() {
        // given
        Order order = new Order(
                new Name("제품명"),
                new Quantity(5)
        );
        Inventory inventory = new Inventory(
                new Quantity(3),
                new Quantity(2)
        );
        Promotion promotion = new Promotion(
                new Name("테스트"),
                new Quantity(1),
                new Quantity(1),
                LocalDate.now(),
                LocalDate.now().plusDays(1)
        );

        // when
        PromotionCommandResponse response = immutableOrderPromotionStrategy.apply(order, inventory, promotion);

        // then
        assertThat(response.giftQuantity().value()).isEqualTo(1);
        assertThat(response.order()).isEqualTo(order);
    }

    @Test
    @DisplayName("프로모션 조건에 알맞게 구입한 경우, 그대로 진행한다.")
    void 프로모션_적용() {
        // given
        Order order = new Order(
                new Name("제품명"),
                new Quantity(6)
        );
        Inventory inventory = new Inventory(
                new Quantity(6),
                new Quantity(0)
        );
        Promotion promotion = new Promotion(
                new Name("테스트"),
                new Quantity(2),
                new Quantity(1),
                LocalDate.now(),
                LocalDate.now().plusDays(1)
        );

        // when
        PromotionCommandResponse response = immutableOrderPromotionStrategy.apply(order, inventory, promotion);

        // then
        assertThat(response.giftQuantity().value()).isEqualTo(2);
        assertThat(response.order()).isEqualTo(order);
    }
}