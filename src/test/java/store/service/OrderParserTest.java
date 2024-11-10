package store.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import store.domain.vo.Name;
import store.domain.vo.Order;
import store.domain.vo.Quantity;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class OrderParserTest {
    private final OrderParser orderParser = new OrderParser();

    @Nested
    class 예외_케이스 {
        @ParameterizedTest
        @DisplayName("공백 문자는 입력할 수 없다.")
        @ValueSource(strings = {"", " ", "  "})
        void 공백_문자_처리(String input) {
            // given
            // when
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> {
                        orderParser.from(input);
                    })
                    .withMessage("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }

        @ParameterizedTest
        @DisplayName("쉼표로 구분되지 않은 경우 예외를 던질 수 있다.")
        @ValueSource(strings = {"[밥-1] [빵-10]", "[과자-10].[음료수-2]"})
        void 주문_구분자(String input) {
            // given
            // when
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> {
                        orderParser.from(input);
                    })
                    .withMessage("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }

        @ParameterizedTest
        @DisplayName("대괄호 형식을 검증할 수 있다.")
        @ValueSource(strings = {"[과자-1)", "(음료수-2]", "과자-3", "[초콜릿-5], 마이쮸-2"})
        void 대괄호_검증_예외(String input) {
            // given
            // when
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> {
                        orderParser.from(input);
                    })
                    .withMessage("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }

        @ParameterizedTest
        @DisplayName("0 ~ 1,000 범위를 범위를 벗어난 값은 입력할 수 없다.")
        @ValueSource(strings = {"[밥-1001], [과자-2]", "[초콜릿--1]"})
        void 주문_숫자_범위(String input) {
            // given
            // when
            assertThatIllegalArgumentException().isThrownBy(() -> {
                orderParser.from(input);
            });
        }
    }

    @Nested
    class 정상_케이스 {
        @Test
        @DisplayName("올바른 입력 형식을 파싱할 수 있다.")
        void 올바른_입력_처리() {
            // given
            String input = "[김밥-1],      [떡볶이-2]";
            Order first = new Order(new Name("김밥"), new Quantity(1));
            Order second = new Order(new Name("떡볶이"), new Quantity(2));

            // when
            List<Order> orders = orderParser.from(input);

            // then
            assertThat(orders).contains(first, second);
        }

        @Test
        @DisplayName("중복되는 항목을 하나로 합칠 수 있다.")
        void 중복_입력_처리() {
            // given
            String input = "[김밥-1],[김밥-4],[김밥-5]";
            Order result = new Order(new Name("김밥"), new Quantity(10));

            // when
            List<Order> orders = orderParser.from(input);

            // then
            assertThat(orders).contains(result);
        }
    }
}