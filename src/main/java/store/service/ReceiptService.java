package store.service;

import store.domain.DiscountInfo;
import store.domain.vo.Order;
import store.domain.Promotion;
import store.domain.vo.*;
import store.repository.order.OrderRepository;
import store.repository.product.ProductRepository;

import java.util.List;
import java.util.Map;

/**
 * 주문 내역, 프로모션 할인 적용 내역, 멤버십 할인 적용 내역을 관리하는 서비스
 */
public class ReceiptService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final DiscountInfo discountInfo;

    public ReceiptService(
            OrderRepository orderRepository, ProductRepository productRepository
    ) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.discountInfo = new DiscountInfo();
    }

    public void reset() {
        orderRepository.deleteAll();
        discountInfo.reset();
    }

    public void setOrder(Order order) {
        validateProduct(order.productName());
        Product product = productRepository.findByName(order.productName());
        orderRepository.save(product, order.quantity());
    }

    public void setOrder(List<Order> orders) {
        orders.forEach(this::setOrder);
    }

    private void validateProduct(Name name) {
        if (productRepository.findByName(name) == null) {
            throw new IllegalStateException(String.format("%s는 상품 목록에 존재하지 않습니다.", name.value()));
        }
    }

    public void addPromotionDiscount(Name name, Quantity quantity) {
        Product product = productRepository.findByName(name);
        if (product == null) {
            throw new IllegalStateException(String.format("%s는 존재하지 않는 상품입니다.", name.value()));
        }
        if (quantity.equals(Quantity.ZERO)) {
            return;
        }
        discountInfo.addPromotionDiscount(product, quantity);
    }

    public void setMembershipDiscount() {
        List<Product> products = productRepository.findAll();
        int notPromotionPrice = 0;
        for (Product product : products) {
            Quantity quantity = orderRepository.findQuantityByProduct(product);
            int notPromotionCount = getNotPromotionCount(product, quantity);
            notPromotionPrice += notPromotionCount * product.price().value();
        }
        discountInfo.setMembershipDiscount(notPromotionPrice);
    }

    private int getNotPromotionCount(Product product, Quantity orderQuantity) {
        Promotion promotion = productRepository.findPromotionByName(product.name());
        if (promotion == null) {
            return orderQuantity.value();
        }
        final int promotionCount = discountInfo.getPromotionCount(product);
        final int promotionBundle = promotion.buyQuantity().value() + promotion.getQuantity().value();
        final int promotionQuantity = promotionCount * promotionBundle;
        return orderQuantity.value() - promotionQuantity;
    }

    public Map<Product, Quantity> getOrderResult() {
        return orderRepository.findAll();
    }

    public Map<Product, Quantity> getPromotionResult() {
        return discountInfo.getPromotionResult();
    }

    public List<Order> getAllOrder() {
        Map<Product, Quantity> orders = orderRepository.findAll();
        return orders.keySet().stream()
                .map(product -> new Order(product.name(), orders.get(product)))
                .toList();
    }

    public int getTotalOrderPrice() {
        List<Product> products = productRepository.findAll();
        int total = 0;
        for (Product product : products) {
            Quantity quantity = orderRepository.findQuantityByProduct(product);
            total += product.price().value() * quantity.value();
        }
        return total;
    }

    public int getTotalPromotionDiscount() {
        return discountInfo.getTotalPromotionDiscount();
    }

    public int getMembershipDiscount() {
        return discountInfo.getMembershipDiscount();
    }

    public int getPayPrice() {
        return getTotalOrderPrice() - discountInfo.getTotalPromotionDiscount() - getMembershipDiscount();
    }
}
