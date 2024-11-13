package store.view.dto.calculate;

public record ReceiptCalculateRequest(
        int orderCount,
        int totalOrderPrice,
        int totalPromotionDiscount,
        int membershipDiscount,
        int payPrice
) {
}
