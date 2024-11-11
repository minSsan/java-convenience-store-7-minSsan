package store.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import store.infrastructure.constant.ExceptionMessage;

import static org.assertj.core.api.Assertions.*;

class PriceTest {
    @Nested
    class 생성자_검증 {
        @ParameterizedTest
        @DisplayName("지정 범위를 벗어나는 값은 설정할 수 없다.")
        @ValueSource(ints = {0, 1, 1_000_001, -1})
        void 범위_이외_값(int input) {
            // given
            // when & then
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> {
                        new Price(input);
                    })
                    .withMessageContaining(ExceptionMessage.WRONG_PRICE_RANGE.message());
        }

        @ParameterizedTest
        @DisplayName("정수형이 아닌 문자열로 생성할 수 없다.")
        @ValueSource(strings = {"0.1", "10.0", "10a", "a"})
        void 숫자_형식(String input) {
            // given
            // when & then
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> {
                        Price.from(input);
                    })
                    .withMessageContaining(ExceptionMessage.WRONG_INTEGER_FORMAT.message());
        }
    }

    @Nested
    class 연산_검증 {
        @ParameterizedTest
        @DisplayName("곱셈 연산을 수행할 수 있다.")
        @CsvSource(value = {"1000, 2", "3232, 999"})
        void 범위_이외_값(int price, int operand) {
            // given
            Price price_ = new Price(price);
            Quantity quantity = new Quantity(operand);

            // when
            final int multiplyWithQuantity = price_.multiply(quantity);
            final int multiplyWithInt = price_.multiply(operand);

            // then
            assertThat(multiplyWithQuantity)
                    .isEqualTo(multiplyWithInt)
                    .isEqualTo(price * operand);
        }
    }
}