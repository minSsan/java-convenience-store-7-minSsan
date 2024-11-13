package store.service;

import store.domain.vo.Name;
import store.domain.vo.Order;
import store.domain.vo.Quantity;
import store.infrastructure.constant.Delimiter;
import store.infrastructure.constant.ExceptionMessage;
import store.infrastructure.exception.CustomException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class OrderParser {
    private final static String ORDER_REGEX =
            "^\\[[a-zA-Z가-힣]+" + Delimiter.ORDER_MENU_DELIMITER + "\\d+\\]";

    public List<Order> from(String input) {
        String[] split = input.split(Delimiter.ORDERS_SPLIT_DELIMITER);
        List<String> orderStrings = Arrays.stream(split).map(String::trim).toList();
        validateOrderFormat(orderStrings);
        List<Order> orders = orderStrings.stream()
                .map(value -> value.substring(1, value.length()-1))
                .map(this::parseStringToOrder)
                .toList();
        return getRemovedDuplicate(orders);
    }

    private List<Order> getRemovedDuplicate(List<Order> orders) {
        HashMap<Name, Quantity> result = new HashMap<>();
        for (Order order : orders) {
            result.put(order.productName(), Quantity.ZERO);
        }
        for (Order order : orders) {
            Quantity summed = result.get(order.productName()).sum(order.quantity());
            result.put(order.productName(), summed);
        }
        return result.keySet().stream().map(name -> new Order(name, result.get(name))).toList();
    }

    private void validateOrderFormat(List<String> input) {
        input.forEach(value -> {
            if (!value.matches(ORDER_REGEX)) {
                throw new CustomException(ExceptionMessage.WRONG_INPUT_FORMAT.message());
            }
        });
    }

    private Order parseStringToOrder(String input) {
        String[] split = input.split(Delimiter.ORDER_MENU_DELIMITER);
        Name name = new Name(split[0]);
        Quantity quantity = Quantity.of(split[1]);
        return new Order(name, quantity);
    }
}
