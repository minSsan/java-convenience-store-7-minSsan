package store.view;

import store.domain.ProductInfo;
import store.infrastructure.constant.Delimiter;
import store.infrastructure.constant.Message;
import store.infrastructure.constant.ValueLength;
import store.view.dto.calculate.ReceiptCalculateRequest;
import store.view.dto.product.ProductInfoDto;
import store.view.dto.product.ProductViewRequest;
import store.view.dto.gift.ReceiptGiftViewRequest;
import store.view.dto.order.ReceiptOrderViewRequest;

public class OutputView {
    private static final int TOTAL_WIDTH = ValueLength.NAME_MAX + ValueLength.PRICE_MAX + 2 * ValueLength.QUANTITY_MAX;
    private static final String RECEIPT_FORMAT =
            "%-" + ValueLength.NAME_MAX + "s"
                    + "%-" + ValueLength.QUANTITY_MAX * 2 + "s"
                    + "%-" + ValueLength.PRICE_MAX * 2 + "s"
                    + Message.NEW_LINE;

    public void printMessage(String message) {
        System.out.println(message);
    }

    public void printWelcome() {
        System.out.println("안녕하세요. W편의점입니다.");
    }

    public void printProducts(ProductViewRequest request) {
        StringBuffer stringBuffer = new StringBuffer();
        request.productInfoDtos.forEach(productInfo -> {
            stringBuffer.append(productInfoText(productInfo));
        });
        System.out.print(stringBuffer);
    }

    private String productInfoText(ProductInfoDto productInfo) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(
                String.format("- %s %,d원 %s", productInfo.name(), productInfo.price(), productInfo.quantity())
        );
        if (productInfo.promotion() != null) {
            stringBuffer.append(String.format(" %s", productInfo.promotion().name().value()));
        }
        stringBuffer.append(Message.NEW_LINE);
        return stringBuffer.toString();
    }

    public void printReceiptTitle() {
        System.out.println(Delimiter.TITLE_PADDING.repeat(TOTAL_WIDTH));
    }

    public void printReceiptTitle(String title) {
        final int paddingCount = (TOTAL_WIDTH - title.length()) / 2;
        String paddingText = Delimiter.TITLE_PADDING.repeat(paddingCount);
        System.out.println(paddingText + title + paddingText);
    }

    public void printReceiptOrderContent(ReceiptOrderViewRequest request) {
        StringBuffer stringBuffer = new StringBuffer(receiptOrderColumTitle());
        request.receiptOrderDtos.forEach(receiptDto -> {
            stringBuffer.append(receiptColumn(receiptDto.name(), receiptDto.quantity(), receiptDto.price()));
        });
        System.out.print(stringBuffer);
    }

    public void printReceiptGiftContent(ReceiptGiftViewRequest request) {
        StringBuffer stringBuffer = new StringBuffer();
        request.receiptGiftDtos.forEach(receiptGiftDto -> {
            stringBuffer.append(receiptColumn(receiptGiftDto.name(), receiptGiftDto.quantity()));
        });
        System.out.print(stringBuffer);
    }

    public void printReceiptCalculateContent(ReceiptCalculateRequest request) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(receiptColumn("총구매액", request.orderCount(), request.totalOrderPrice()));
        stringBuffer.append(receiptColumnCost("행사할인", String.format("-%,d", request.totalPromotionDiscount())));
        stringBuffer.append(receiptColumnCost("멤버십할인", String.format("-%,d", request.membershipDiscount())));
        stringBuffer.append(receiptColumnCost("내실돈", String.format("%,d", request.payPrice())));
        System.out.print(stringBuffer);
    }

    private String receiptOrderColumTitle() {
        return String.format(RECEIPT_FORMAT, "상품명", "수량", "금액");
    }

    private String receiptColumn(String name, int count) {
        String formatCount = String.format("%,d", count);
        return String.format(RECEIPT_FORMAT, name, formatCount, "");
    }

    private String receiptColumn(String name, int count, int price) {
        String formatCount = String.format("%,d", count);
        String formatPrice = String.format("%,d", price);
        return String.format(RECEIPT_FORMAT, name, formatCount, formatPrice);
    }

    private String receiptColumnCost(String name, String cost) {
        return String.format(RECEIPT_FORMAT, name, "", cost);
    }
}
