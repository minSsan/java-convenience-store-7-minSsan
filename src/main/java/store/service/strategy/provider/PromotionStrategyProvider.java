package store.service.strategy.provider;

import store.domain.vo.PromotionOption;
import store.service.strategy.AddFreePromotionStrategy;
import store.service.strategy.ExcludeNonApplicablePromotionStrategy;
import store.service.strategy.ImmutableOrderPromotionStrategy;
import store.service.strategy.PromotionStrategy;

import java.util.Arrays;

public enum PromotionStrategyProvider {
    ADD_FREE_AGREE(PromotionOption.ADD_FREE, true, new AddFreePromotionStrategy()),
    ADD_FREE_DISAGREE(PromotionOption.ADD_FREE, false, new ImmutableOrderPromotionStrategy()),
    REGULAR_PURCHASE_AGREE(PromotionOption.REGULAR_PURCHASE, true, new ImmutableOrderPromotionStrategy()),
    REGULAR_PURCHASE_DISAGREE(
            PromotionOption.REGULAR_PURCHASE,
            false,
            new ExcludeNonApplicablePromotionStrategy()
    );

    private final PromotionOption option;
    private final boolean answer;
    private final PromotionStrategy strategy;

    private PromotionStrategyProvider(PromotionOption option, boolean answer, PromotionStrategy strategy) {
        this.option = option;
        this.answer = answer;
        this.strategy = strategy;
    }

    public static PromotionStrategy from(PromotionOption option, boolean answer) {
        PromotionStrategyProvider strategyProvider = Arrays.stream(values())
                .filter(value -> value.option.equals(option) && value.answer == answer)
                .findFirst()
                .orElse(null);
        if (strategyProvider == null) {
            return new ImmutableOrderPromotionStrategy();
        }
        return strategyProvider.strategy;
    }
}
