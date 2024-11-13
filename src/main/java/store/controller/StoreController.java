package store.controller;

import store.infrastructure.constant.Message;
import store.service.OrderParser;
import store.domain.vo.Name;
import store.domain.vo.Order;
import store.domain.vo.Quantity;
import store.service.*;
import store.service.dto.request.ApplyPromotionRequest;
import store.service.dto.response.PromotionCommandResponse;
import store.service.dto.response.PromotionQueryResponse;
import store.domain.vo.PromotionOption;
import store.service.strategy.PromotionStrategy;
import store.service.strategy.provider.PromotionStrategyProvider;
import store.validator.OrderValidator;
import store.view.InputView;
import store.view.OutputView;
import store.view.dto.calculate.ReceiptCalculateRequest;
import store.view.dto.gift.ReceiptGiftViewRequest;
import store.view.dto.order.ReceiptOrderViewRequest;
import store.view.dto.product.ProductViewRequest;

import java.util.List;

public class StoreController {
    private final StoreFileInputService storeFileInputService;
    private final ProductService productService;
    private final PromotionService promotionService;
    private final OrderValidator orderValidator;
    private final OrderParser orderParser;
    private final ReceiptService receiptService;
    private final InputView inputView;
    private final OutputView outputView;

    public StoreController(
            StoreFileInputService storeFileInputService,
            ProductService productService,
            PromotionService promotionService,
            OrderValidator orderValidator,
            OrderParser orderParser,
            ReceiptService receiptService,
            InputView inputView,
            OutputView outputView
    ) {
        this.storeFileInputService = storeFileInputService;
        this.productService = productService;
        this.promotionService = promotionService;
        this.orderValidator = orderValidator;
        this.orderParser = orderParser;
        this.receiptService = receiptService;
        this.inputView = inputView;
        this.outputView = outputView;
    }

    public void run() {
        storeFileInputService.loadAndSave();
        while (true) {
            printInfo();
            receiveOrder();
            handleDiscount();
            applyOrder();
            printReceipt();
            resetOrder();
            if (!isContinue()) break;
        }
    }

    private boolean isContinue() {
        return inputView.readAdditionalPurchase();
    }

    private void printInfo() {
        outputView.printWelcome();
        outputView.printProducts(new ProductViewRequest(productService.getAllProductInfo()));
    }

    private void printReceipt() {
        printReceiptOrder();
        printReceiptGift();
        printReceiptCalculate();
    }

    private void printReceiptOrder() {
        outputView.printReceiptTitle(Message.STORE_NAME);
        outputView.printReceiptOrderContent(new ReceiptOrderViewRequest(receiptService.getOrderResult()));
    }

    private void printReceiptGift() {
        outputView.printReceiptTitle("증    정");
        outputView.printReceiptGiftContent(new ReceiptGiftViewRequest(receiptService.getPromotionResult()));
    }

    private void printReceiptCalculate() {
        outputView.printReceiptTitle();
        outputView.printReceiptCalculateContent(
                new ReceiptCalculateRequest(
                        receiptService.getOrderResult().size(),
                        receiptService.getTotalOrderPrice(),
                        receiptService.getTotalPromotionDiscount(),
                        receiptService.getMembershipDiscount(),
                        receiptService.getPayPrice()
                ));
    }

    private void receiveOrder() {
        while (true) {
            try {
                List<Order> orders = orderParser.from(inputView.readItem());
                orderValidator.validate(orders);
                receiptService.setOrder(orders);
                break;
            } catch (IllegalArgumentException e) {
                outputView.printMessage(e.getMessage());
            }
        }
    }

    private void handleDiscount() {
        handlePromotion();
        handleMembership();
    }

    private void handlePromotion() {
        List<Order> orders = receiptService.getAllOrder();
        for (Order order : orders) {
            if (promotionService.isExistApplicablePromotion(order.productName())) {
                PromotionQueryResponse queryResult = promotionService.query(order);
                PromotionCommandResponse response = getPromotionResult(order, queryResult);
                receiptService.setOrder(response.order());
                receiptService.addPromotionDiscount(order.productName(), response.giftQuantity());
            }
        }
    }

    private PromotionCommandResponse getPromotionResult(Order order, PromotionQueryResponse queryResponse) {
        if (queryResponse.option().equals(PromotionOption.NONE)) {
            return new PromotionCommandResponse(order, queryResponse.giftQuantity());
        }
        PromotionStrategy strategy = getStrategy(order, queryResponse);

        ApplyPromotionRequest request = new ApplyPromotionRequest(order, strategy);
        return promotionService.command(request);
    }

    private PromotionStrategy getStrategy(Order order, PromotionQueryResponse queryResponse) {
        if (queryResponse.option().equals(PromotionOption.ADD_FREE)) {
            return getAddFreeStrategy(order.productName());
        }
        return getRegularPurchaseStrategy(order.productName(), queryResponse.notAppliedQuantity());
    }

    private PromotionStrategy getAddFreeStrategy(Name productName) {
        final boolean answer = inputView.readAddFreeAnswer(productName.value());
        return PromotionStrategyProvider.from(PromotionOption.ADD_FREE, answer);
    }

    private PromotionStrategy getRegularPurchaseStrategy(Name productName, Quantity excluded) {
        final boolean answer = inputView.readPaidRegularPriceAnswer(productName.value(), excluded.value());
        return PromotionStrategyProvider.from(PromotionOption.REGULAR_PURCHASE, answer);
    }

    private void handleMembership() {
        final boolean apply = inputView.readMembershipAnswer();
        if (apply) {
            receiptService.setMembershipDiscount();
        }
    }

    private void applyOrder() {
        productService.applyReceiptInventory();
    }

    private void resetOrder() {
        receiptService.reset();
    }
}
