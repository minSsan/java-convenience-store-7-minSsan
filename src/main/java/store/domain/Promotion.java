package store.domain;

import camp.nextstep.edu.missionutils.DateTimes;
import store.domain.vo.*;
import store.infrastructure.constant.ExceptionMessage;
import store.domain.vo.PromotionOption;

import java.time.LocalDate;

public record Promotion(Name name, Quantity buyQuantity, Quantity getQuantity, LocalDate start, LocalDate end) {
    public static final int GET = 1;

    public Promotion {
        validateGet(getQuantity);
        validateDate(start, end);
    }

    public static Promotion of(String name, int buy, int get, LocalDate start, LocalDate end) {
        return new Promotion(new Name(name), new Quantity(buy), new Quantity(get), start, end);
    }

    private void validateGet(Quantity get) {
        if (!get.isSameWith(GET)) {
            throw new IllegalStateException(ExceptionMessage.WRONG_PROMOTION_GET_COUNT.message());
        }
    }

    private void validateDate(LocalDate start, LocalDate end) {
        if (start.isAfter(end)) {
            throw new IllegalStateException(ExceptionMessage.WRONG_PROMOTION_DATE.message());
        }
    }

    public boolean isApplicable() {
        LocalDate now = DateTimes.now().toLocalDate();

        final boolean isAfterOrOnStart = start.isEqual(now) || start.isBefore(now);
        final boolean isBeforeOrOnEnd = end.isEqual(now) || end.isAfter(now);

        return isAfterOrOnStart && isBeforeOrOnEnd;
    }

    public PromotionQueryResult getQueryResult(Quantity quantity) {
        Quantity giftedQuantity = getGiftedQuantity(quantity);
        Quantity appliedQuantity = getAppliedQuantity(giftedQuantity);
        Quantity notAppliedQuantity = quantity.subtract(appliedQuantity);
        return new PromotionQueryResult(giftedQuantity, appliedQuantity, notAppliedQuantity);
    }

    public PromotionOption getPromotionOption(Quantity quantity, Inventory inventory) {
        if (!isApplicable() || quantity.isGreaterThan(inventory.getTotal())) return PromotionOption.NONE;

        PromotionQueryResult queryResult = getQueryResult(Quantity.min(quantity, inventory.promotion()));
        if (isPossibleAddFree(quantity, inventory, queryResult.nonApplied())) {
            return PromotionOption.ADD_FREE;
        }
        if (quantity.subtract(queryResult.applied()).isGreaterThan(0)) {
            return PromotionOption.REGULAR_PURCHASE;
        }
        return PromotionOption.NONE;
    }

    private boolean isPossibleAddFree(Quantity quantity, Inventory inventory, Quantity notApplied) {
        Quantity added = quantity.sum(getQuantity);
        Quantity promotionQuantity = inventory.promotion();
        return notApplied.equals(buyQuantity)
                && (added.isSmallerThan(promotionQuantity) || added.isSameWith(promotionQuantity));
    }

    private Quantity getGiftedQuantity(Quantity quantity) {
        if (!this.isApplicable()) {
            return Quantity.ZERO;
        }
        Quantity promotionUnitCount = buyQuantity.sum(getQuantity);
        return quantity.division(promotionUnitCount);
    }

    private Quantity getAppliedQuantity(Quantity giftQuantity) {
        Quantity promotionUnitCount = buyQuantity.sum(getQuantity);
        return giftQuantity.multiply(promotionUnitCount);
    }
}
