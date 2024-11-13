package store.infrastructure.config;

import store.controller.StoreController;
import store.repository.inventory.InventoryRepository;
import store.repository.inventory.InventoryRepositoryImpl;
import store.repository.order.OrderRepository;
import store.repository.order.OrderRepositoryImpl;
import store.repository.product.ProductRepository;
import store.repository.product.ProductRepositoryImpl;
import store.repository.promotion.PromotionRepository;
import store.repository.promotion.PromotionRepositoryImpl;
import store.service.*;
import store.validator.OrderValidator;
import store.view.InputView;
import store.view.OutputView;

public class AppConfig {
    private ProductRepository productRepository;
    private InventoryRepository inventoryRepository;
    private OrderRepository orderRepository;
    private PromotionRepository promotionRepository;

    private ProductRepository productRepository() {
        return new ProductRepositoryImpl();
    }

    private InventoryRepository inventoryRepository() {
        return new InventoryRepositoryImpl();
    }

    private OrderRepository orderRepository() {
        return new OrderRepositoryImpl();
    }

    private PromotionRepository promotionRepository() {
        return new PromotionRepositoryImpl();
    }

    public ProductRepository getProductRepository() {
        if (this.productRepository == null) {
            this.productRepository = productRepository();
        }
        return this.productRepository;
    }

    public InventoryRepository getInventoryRepository() {
        if (this.inventoryRepository == null) {
            this.inventoryRepository = inventoryRepository();
        }
        return this.inventoryRepository;
    }

    public OrderRepository getOrderRepository() {
        if (this.orderRepository == null) {
            this.orderRepository = orderRepository();
        }
        return this.orderRepository;
    }

    public PromotionRepository getPromotionRepository() {
        if (this.promotionRepository == null) {
            this.promotionRepository = promotionRepository();
        }
        return this.promotionRepository;
    }

    public StoreFileInputService getStoreFileInputService() {
        return new StoreFileInputService(
                getProductRepository(),
                getPromotionRepository(),
                getInventoryRepository()
        );
    }

    public ProductService getProductService() {
        return new ProductService(
                getOrderRepository(),
                getProductRepository(),
                getInventoryRepository()
        );
    }

    public PromotionService getPromotionService() {
        return new PromotionService(
                getProductRepository(),
                getInventoryRepository()
        );
    }

    public OrderValidator getOrderValidator() {
        return new OrderValidator(
                getProductRepository(),
                getInventoryRepository()
        );
    }

    public OrderParser getOrderParser() {
        return new OrderParser();
    }

    public ReceiptService getDiscountService() {
        return new ReceiptService(
                getOrderRepository(),
                getProductRepository()
        );
    }

    public InputView getInputView() {
        return new InputView();
    }

    public OutputView getOutputView() {
        return new OutputView();
    }

    public StoreController controller() {
        return new StoreController(
                getStoreFileInputService(),
                getProductService(),
                getPromotionService(),
                getOrderValidator(),
                getOrderParser(),
                getDiscountService(),
                getInputView(),
                getOutputView()
        );
    }
}
