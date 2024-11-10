package store.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.Promotion;
import store.domain.vo.*;
import store.repository.inventory.InventoryRepositoryImpl;
import store.repository.product.ProductRepositoryImpl;
import store.repository.promotion.PromotionRepositoryImpl;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class StoreFileInputServiceTest {
    private final ProductRepositoryImpl productRepository = new ProductRepositoryImpl();
    private final PromotionRepositoryImpl promotionRepository = new PromotionRepositoryImpl();
    private final InventoryRepositoryImpl inventoryRepository = new InventoryRepositoryImpl();
    private final StoreFileInputService storeFileInputService = new StoreFileInputService(
            productRepository,
            promotionRepository,
            inventoryRepository
    );

    enum PromotionType {
        탄산(new Promotion(
                new Name("탄산2+1"),
                new Quantity(2),
                new Quantity(1),
                LocalDate.of(2024,1,1),
                LocalDate.of(2024,12,31)
        )),
        MD추천(new Promotion(
                new Name("MD추천상품"),
                new Quantity(1),
                new Quantity(1),
                LocalDate.of(2024,1,1),
                LocalDate.of(2024,12,31)
        )),
        반짝할인(new Promotion(
                new Name("반짝할인"),
                new Quantity(1),
                new Quantity(1),
                LocalDate.of(2024,11,1),
                LocalDate.of(2024,11,30)
        )),
        NULL(null),
        ;

        private Promotion promotion;

        private PromotionType(Promotion promotion) {
            this.promotion = promotion;
        }

        public Promotion getPromotion() {
            return promotion;
        }
    }

    @Test
    @DisplayName("파일 내용을 정상적으로 읽어올 수 있다.")
    void 파일_로드_테스트() {
        // given
        List<Product> products = List.of(
                new Product(new Name("콜라"), new Price(1000)),
                new Product(new Name("사이다"), new Price(1000)),
                new Product(new Name("오렌지주스"), new Price(1800)),
                new Product(new Name("탄산수"), new Price(1200)),
                new Product(new Name("물"), new Price(500)),
                new Product(new Name("비타민워터"), new Price(1500)),
                new Product(new Name("감자칩"), new Price(1500)),
                new Product(new Name("초코바"), new Price(1200)),
                new Product(new Name("에너지바"), new Price(2000)),
                new Product(new Name("정식도시락"), new Price(6400)),
                new Product(new Name("컵라면"), new Price(1700))
        );

        HashMap<Name, PromotionType> promotions = new HashMap<>() {{
            put(new Name("콜라"), PromotionType.탄산);
            put(new Name("사이다"), PromotionType.탄산);
            put(new Name("오렌지주스"), PromotionType.MD추천);
            put(new Name("탄산수"), PromotionType.탄산);
            put(new Name("물"), PromotionType.NULL);
            put(new Name("비타민워터"), PromotionType.NULL);
            put(new Name("감자칩"), PromotionType.반짝할인);
            put(new Name("초코바"), PromotionType.MD추천);
            put(new Name("에너지바"), PromotionType.NULL);
            put(new Name("정식도시락"), PromotionType.NULL);
            put(new Name("컵라면"), PromotionType.MD추천);
        }};

        HashMap<Name, Inventory> inventory = new HashMap<>() {{
            put(new Name("콜라"), new Inventory(new Quantity(10), new Quantity(10)));
            put(new Name("사이다"), new Inventory(new Quantity(8), new Quantity(7)));
            put(new Name("오렌지주스"), new Inventory(new Quantity(9), new Quantity(0)));
            put(new Name("탄산수"), new Inventory(new Quantity(5), new Quantity(0)));
            put(new Name("물"), new Inventory(new Quantity(0), new Quantity(10)));
            put(new Name("비타민워터"), new Inventory(new Quantity(0), new Quantity(6)));
            put(new Name("감자칩"), new Inventory(new Quantity(5), new Quantity(5)));
            put(new Name("초코바"), new Inventory(new Quantity(5), new Quantity(5)));
            put(new Name("에너지바"), new Inventory(new Quantity(0), new Quantity(5)));
            put(new Name("정식도시락"), new Inventory(new Quantity(0), new Quantity(8)));
            put(new Name("컵라면"), new Inventory(new Quantity(1), new Quantity(10)));
        }};

        // when
        storeFileInputService.loadAndSave();

        // then
        products.forEach(product -> {
            Product foundProduct = productRepository.findByName(product.name());
            Promotion foundPromotion = productRepository.findPromotionByName(product.name());
            Inventory foundInventory = inventoryRepository.findByProductName(product.name());

            assertThat(foundProduct).isNotNull();
            assertThat(foundPromotion).isEqualTo(promotions.get(product.name()).getPromotion());
            assertThat(foundInventory).isEqualTo(inventory.get(product.name()));
        });
    }
}