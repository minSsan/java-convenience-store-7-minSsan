package store.infrastructure.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.repository.inventory.InventoryRepository;
import store.repository.order.OrderRepository;
import store.repository.product.ProductRepository;

import static org.assertj.core.api.Assertions.assertThat;

class AppConfigTest {
    AppConfig appConfig = new AppConfig();

    @Test
    @DisplayName("항상 동일한 인벤토리 레파지토리 객체를 반환할 수 있다.")
    void 동일_인벤토리_레파지토리_구현체_반환() {
        // given
        InventoryRepository firstGetResult = appConfig.getInventoryRepository();

        // when
        InventoryRepository secondGetResult = appConfig.getInventoryRepository();

        // then
        assertThat(secondGetResult).isSameAs(firstGetResult);
    }

    @Test
    @DisplayName("항상 동일한 주문 레파지토리 객체를 반환할 수 있다.")
    void 동일_주문_레파지토리_구현체_반환() {
        // given
        OrderRepository firstGetResult = appConfig.getOrderRepository();

        // when
        OrderRepository secondGetResult = appConfig.getOrderRepository();

        // then
        assertThat(secondGetResult).isSameAs(firstGetResult);
    }

    @Test
    @DisplayName("항상 동일한 상품 레파지토리 객체를 반환할 수 있다.")
    void 동일_상품_레파지토리_구현체_반환() {
        // given
        ProductRepository firstGetResult = appConfig.getProductRepository();

        // when
        ProductRepository secondGetResult = appConfig.getProductRepository();

        // then
        assertThat(secondGetResult).isSameAs(firstGetResult);
    }
}