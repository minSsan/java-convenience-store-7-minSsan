package store.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import store.domain.vo.*;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

class PromotionTest {
    private final String name = "상품명";

    @Nested
    class 생성자_검증_테스트 {
        @Test
        @DisplayName("[예외] 프로모션 종료 기간은 시작 기간을 앞설 수 없다.")
        void 프로모션_기간_유효성_검증() {
            // given
            LocalDate start = LocalDate.now();
            LocalDate end = start.minusDays(1);

            // when & then
            assertThatIllegalStateException().isThrownBy(() -> {
                Promotion.of(name, 1, 1, start, end);
            });
        }

        @Test
        @DisplayName("[예외] 정책에 맞지 않는 프로모션은 생성할 수 없다.")
        void 프로모션_정책_검증() {
            // given
            LocalDate start = LocalDate.now();
            LocalDate end = start.plusDays(1);
            final int wrongGetCount = 2;

            // when & then
            assertThatIllegalStateException().isThrownBy(() -> {
                Promotion.of(name, 1, wrongGetCount, start, end);
            });
        }
    }

    @Nested
    class 프로모션_기간_판별_테스트 {
        @Test
        @DisplayName("현재 시점을 기준으로 적용 불가능한 프로모션을 판별할 수 있다.")
        void 적용_불가능한_프로모션_기간_확인() {
            // given
            LocalDate start = LocalDate.now().minusDays(7);
            LocalDate end = start.plusDays(1);
            Promotion promotion = Promotion.of(name, 1, 1, start, end);

            // when
            final boolean applicable = promotion.isApplicable();

            // then
            assertThat(applicable).isFalse();
        }

        @Test
        @DisplayName("현재 시점을 기준으로 적용 가능한 프로모션을 판별할 수 있다.")
        void 적용_가능한_프로모션_기간_확인() {
            // given
            LocalDate start = LocalDate.now();
            LocalDate end = start.plusDays(1);
            Promotion promotion = Promotion.of(name, 1, 1, start, end);

            // when
            final boolean applicable = promotion.isApplicable();

            // then
            assertThat(applicable).isTrue();
        }
    }

    @Nested
    class 프로모션_쿼리_결과_테스트 {
        @ParameterizedTest
        @DisplayName("올바른 프로모션 적용 결과를 반환할 수 있다.")
        @CsvSource(value = {
                "1, 1, 5, 2, 4, 1",
                "2, 1, 3, 1, 3, 0",
                "2, 1, 2, 0, 0, 2"
        })
        void 프로모션_적용_결과_반환(int buy, int get, int order, int gifted, int applied, int notApplied) {
            // given
            Promotion promotion = Promotion.of(
                    name,
                    buy,
                    get,
                    LocalDate.now(),
                    LocalDate.now().plusDays(1)
            );
            Quantity orderQuantity = new Quantity(order);

            // when
            PromotionQueryResult queryResult = promotion.getQueryResult(orderQuantity);

            // then
            assertThat(queryResult).isEqualTo(new PromotionQueryResult(
                    new Quantity(gifted),
                    new Quantity(applied),
                    new Quantity(notApplied)
            ));
        }
    }

    @Nested
    class 프로모션_옵션_반환_테스트 {
        Promotion promotion = new Promotion(
                new Name("이름"),
                new Quantity(2),
                new Quantity(1),
                LocalDate.now(),
                LocalDate.now().plusDays(1)
        );

        @Test
        @DisplayName("프로모션 제품을 증정받을 수 있는 경우 추가 옵션을 반환한다.")
        void 추가_옵션_반환() {
            // given
            Quantity orderQuantity = new Quantity(5);
            Inventory inventory = new Inventory(
                    new Quantity(6),
                    Quantity.ZERO
            );

            // when
            PromotionOption promotionOption = promotion.getPromotionOption(orderQuantity, inventory);

            // then
            assertThat(promotionOption).isEqualTo(PromotionOption.ADD_FREE);
        }

        @Test
        @DisplayName("재고 부족으로 정가 구매가 필요한 경우 정가 구매 옵션을 반환한다.")
        void 정가_옵션_반환() {
            // given
            Quantity orderQuantity = new Quantity(5);
            Inventory inventory = new Inventory(
                    new Quantity(3),
                    new Quantity(2)
            );

            // when
            PromotionOption promotionOption = promotion.getPromotionOption(orderQuantity, inventory);

            // then
            assertThat(promotionOption).isEqualTo(PromotionOption.REGULAR_PURCHASE);
        }

        @Test
        @DisplayName("재고가 부족한 경우에는 NONE 옵션을 반환한다.")
        void 재고_부족_NONE_옵션_반환() {
            // given
            Quantity orderQuantity = new Quantity(5);
            Inventory inventory = new Inventory(
                    new Quantity(2),
                    new Quantity(2)
            );

            // when
            PromotionOption promotionOption = promotion.getPromotionOption(orderQuantity, inventory);

            // then
            assertThat(promotionOption).isEqualTo(PromotionOption.NONE);
        }

        @Test
        @DisplayName("프로모션 재고 범위 내에 주문한 경우에는 NONE 옵션을 반환한다.")
        void 재고_충분_NONE_옵션_반환() {
            // given
            Quantity orderQuantity = new Quantity(6);
            Inventory inventory = new Inventory(
                    new Quantity(10),
                    new Quantity(0)
            );

            // when
            PromotionOption promotionOption = promotion.getPromotionOption(orderQuantity, inventory);

            // then
            assertThat(promotionOption).isEqualTo(PromotionOption.NONE);
        }
    }
}