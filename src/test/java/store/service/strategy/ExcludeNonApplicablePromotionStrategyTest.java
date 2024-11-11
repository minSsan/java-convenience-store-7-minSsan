package store.service.strategy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.Promotion;
import store.domain.vo.Inventory;
import store.domain.vo.Name;
import store.domain.vo.Order;
import store.domain.vo.Quantity;
import store.service.dto.response.PromotionCommandResponse;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class ExcludeNonApplicablePromotionStrategyTest {
    private final ExcludeNonApplicablePromotionStrategy excludeNonApplicablePromotionStrategy
            = new ExcludeNonApplicablePromotionStrategy();

    @Test
    @DisplayName("프로모션 미적용 제품이 있는 경우, 이를 제외하고 진행한다.")
    void 프로모션_미적용_제품_구입() {
        // given
        final int promotionCount = 3;
        final int normalCount = 2;
        final int orderCount = 5;
        final int get = 1;
        final int buy = 1;
        final int notApplied = orderCount - orderCount / (get + buy);

        Order order = new Order(
                new Name("제품명"),
                new Quantity(orderCount)
        );
        Inventory inventory = new Inventory(
                new Quantity(promotionCount),
                new Quantity(normalCount)
        );
        Promotion promotion = new Promotion(
                new Name("테스트"),
                new Quantity(1),
                new Quantity(1),
                LocalDate.now(),
                LocalDate.now().plusDays(1)
        );

        // when
        PromotionCommandResponse response = excludeNonApplicablePromotionStrategy.apply(order, inventory, promotion);

        // then
        assertThat(response.giftQuantity().value()).isEqualTo(1);
        assertThat(response.order().quantity().value())
                .isEqualTo(order.quantity().value() - notApplied);
    }
}