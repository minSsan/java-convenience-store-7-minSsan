package store.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.Promotion;
import store.domain.vo.*;
import store.repository.inventory.InventoryRepository;
import store.repository.product.ProductRepository;
import store.service.dto.request.ApplyPromotionRequest;
import store.service.dto.response.PromotionCommandResponse;
import store.service.strategy.AddFreePromotionStrategy;
import store.service.strategy.ExcludeNonApplicablePromotionStrategy;
import store.service.strategy.ImmutableOrderPromotionStrategy;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PromotionServiceTest {
    private final Name promotionName = new Name("프로모션");
    private final Name productName = new Name("상품");

    private final Quantity canAddFreeQuantity = new Quantity(2);
    private final Quantity lackPromotionQuantity = new Quantity(6);

    private final Promotion promotion = new Promotion(
            promotionName,
            new Quantity(2),
            new Quantity(1),
            LocalDate.now(),
            LocalDate.now().plusDays(1)
    );
    private final Product product = new Product(
            productName,
            new Price(1500)
    );
    private final Inventory inventory = new Inventory(
            new Quantity(5),
            new Quantity(5)
    );

    class TestProductRepository implements ProductRepository {

        @Override
        public List<Product> findAll() {
            return null;
        }

        @Override
        public Promotion findPromotionByName(Name name) {
            return promotion;
        }

        @Override
        public Product findByName(Name name) {
            return product;
        }

        @Override
        public void save(Product product, Promotion promotion) {}
    }

    class TestInventoryRepository implements InventoryRepository {

        @Override
        public Inventory findByProductName(Name name) {
            return inventory;
        }

        @Override
        public void save(Name name, Inventory inventory) {}
    }

    private final PromotionService promotionService = new PromotionService(
            new TestProductRepository(), new TestInventoryRepository()
    );

    @Test
    @DisplayName("프로모션 제품 추가 전략을 처리할 수 있다.")
    void 프로모션_제품_추가() {
        // given
        Order order = new Order(productName, canAddFreeQuantity);
        ApplyPromotionRequest request = new ApplyPromotionRequest(
                order,
                new AddFreePromotionStrategy()
        );
        Quantity expectQuantity = order.quantity().sum(new Quantity(1));

        // when
        PromotionCommandResponse response = promotionService.command(request);
        Quantity resultOrderQuantity = response.order().quantity();

        // then
        assertThat(resultOrderQuantity).isEqualTo(expectQuantity);
    }

    @Test
    @DisplayName("프로모션 제품 추가 무시 전략을 처리할 수 있다.")
    void 프로모션_추가_무시() {
        // given
        Order order = new Order(productName, canAddFreeQuantity);
        ApplyPromotionRequest request = new ApplyPromotionRequest(
                order,
                new ImmutableOrderPromotionStrategy()
        );

        // when
        PromotionCommandResponse response = promotionService.command(request);
        Quantity resultOrderQuantity = response.order().quantity();

        // then
        assertThat(resultOrderQuantity).isEqualTo(order.quantity());
    }

    @Test
    @DisplayName("프로모션 제품 제외 전략을 처리할 수 있다.")
    void 프로모션_제외() {
        // given
        Order order = new Order(productName, lackPromotionQuantity);
        ApplyPromotionRequest request = new ApplyPromotionRequest(
                order,
                new ExcludeNonApplicablePromotionStrategy()
        );

        // when
        PromotionCommandResponse response = promotionService.command(request);
        Quantity resultOrderQuantity = response.order().quantity();

        // then
        assertThat(resultOrderQuantity).isEqualTo(new Quantity(3));
    }
}