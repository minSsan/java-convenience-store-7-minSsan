package store.service.strategy.provider;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.service.strategy.AddFreePromotionStrategy;
import store.service.strategy.ExcludeNonApplicablePromotionStrategy;
import store.service.strategy.ImmutableOrderPromotionStrategy;
import store.service.strategy.PromotionStrategy;
import store.domain.vo.PromotionOption;

import static org.assertj.core.api.Assertions.assertThat;

class PromotionStrategyProviderTest {
    @Test
    @DisplayName("프로모션 제품 추가 요청에 따라 올바른 전략을 선택할 수 있다.")
    void 프로모션_제품_추가_전략_선택() {
        // given
        PromotionOption option = PromotionOption.ADD_FREE;
        final boolean answer = true;

        // when
        PromotionStrategy strategy = PromotionStrategyProvider.from(option, answer);

        // then
        assertThat(strategy).isInstanceOf(AddFreePromotionStrategy.class);
    }

    @Test
    @DisplayName("프로모션 제품 추가 거절에 따라 올바른 전략을 선택할 수 있다.")
    void 프로모션_제품_미추가_전략_선택() {
        // given
        PromotionOption option = PromotionOption.ADD_FREE;
        final boolean answer = false;

        // when
        PromotionStrategy strategy = PromotionStrategyProvider.from(option, answer);

        // then
        assertThat(strategy).isInstanceOf(ImmutableOrderPromotionStrategy.class);
    }

    @Test
    @DisplayName("정가 결제 수락에 따라 올바른 전략을 선택할 수 있다.")
    void 정가_결제_수락_전략_선택() {
        // given
        PromotionOption option = PromotionOption.REGULAR_PURCHASE;
        final boolean answer = true;

        // when
        PromotionStrategy strategy = PromotionStrategyProvider.from(option, answer);

        // then
        assertThat(strategy).isInstanceOf(ImmutableOrderPromotionStrategy.class);
    }

    @Test
    @DisplayName("정가 결제 거절에 따라 올바른 전략을 선택할 수 있다.")
    void 정가_결제_거절_전략_선택() {
        // given
        PromotionOption option = PromotionOption.REGULAR_PURCHASE;
        final boolean answer = false;

        // when
        PromotionStrategy strategy = PromotionStrategyProvider.from(option, answer);

        // then
        assertThat(strategy).isInstanceOf(ExcludeNonApplicablePromotionStrategy.class);
    }
}