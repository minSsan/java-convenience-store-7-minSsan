package store.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import store.domain.vo.Name;
import store.domain.vo.Price;
import store.domain.vo.Product;
import store.domain.vo.Quantity;
import store.infrastructure.constant.Membership;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

class DiscountInfoTest {
    private DiscountInfo discountInfo;

    @BeforeEach
    void setup() {
        this.discountInfo = new DiscountInfo();
    }

    @Nested
    class 할인_테스트 {
        @Test
        @DisplayName("[예외] 할인 최대 적용 금액은 8,000원이다.")
        void 할인_적용_금액_초과() {
            // given
            final int total = 1_000_000;

            // when
            discountInfo.setMembershipDiscount(total);
            final int membershipDiscount = discountInfo.getMembershipDiscount();

            // then
            assertThat(membershipDiscount).isEqualTo(Membership.MAX);
        }

        @Test
        @DisplayName("[정상] 주어진 금액에 멤버십 할인을 적용할 수 있다.")
        void 멤버십_할인_적용() {
            // given
            final int total = 5_000;

            // when
            discountInfo.setMembershipDiscount(total);
            final int membershipDiscount = discountInfo.getMembershipDiscount();

            // then
            assertThat(membershipDiscount).isEqualTo(total * Membership.DISCOUNT_RATE / 100);
        }

        @ParameterizedTest
        @DisplayName("[정상] 프로모션 할인을 입력받고 계산할 수 있다.")
        @CsvSource(value = {"1번, 2, 1000", "2번, 3, 9876"})
        void 프로모션_할인(String name, int count, int price) {
            // given
            Product product = new Product(
                    new Name(name),
                    new Price(price)
            );
            Quantity quantity = new Quantity(count);

            // when
            discountInfo.addPromotionDiscount(product, quantity);
            Map<Product, Quantity> promotionResult = discountInfo.getPromotionResult();
            final int totalPromotionDiscount = discountInfo.getTotalPromotionDiscount();

            // then
            assertThat(totalPromotionDiscount).isEqualTo(price * count);
            assertThat(promotionResult).contains(entry(product, quantity));
        }
    }

    @Nested
    class 초기화_테스트 {
        @Test
        @DisplayName("[정상] 기존 값을 초기화할 수 있다.")
        void 초기화() {
            // given
            discountInfo.setMembershipDiscount(999);
            discountInfo.addPromotionDiscount(
                    new Product(new Name("이름1"), new Price(1000)),
                    new Quantity(1)
            );

            // when & then
            assertThat(discountInfo.getMembershipDiscount()).isNotZero();
            assertThat(discountInfo.getTotalPromotionDiscount()).isNotZero();
            discountInfo.reset();
            assertThat(discountInfo.getMembershipDiscount()).isZero();
            assertThat(discountInfo.getTotalPromotionDiscount()).isZero();
        }
    }
}