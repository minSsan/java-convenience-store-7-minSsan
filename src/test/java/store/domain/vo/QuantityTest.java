package store.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

class QuantityTest {
    @ParameterizedTest
    @DisplayName("[예외] 숫자 형식이 아닌 문자열 입력")
    @ValueSource(strings = {"", "h", "1.23", "10a"})
    void 숫자_형식이_아닌_문자열을_입력할_수_없다(String input) {
        // given

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> {
            Quantity.of(input);
        });
    }

    @ParameterizedTest
    @DisplayName("[예외] 범위를 넘어가는 경우")
    @ValueSource(ints = {-1, 1001})
    void 주어진_범위를_넘는_숫자는_입력할_수_없다(int input) {
        // given

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> {
            new Quantity(input);
        });
    }

    @ParameterizedTest
    @DisplayName("[정상] 서로 다른 객체끼리 뺄셈 연산을 처리할 수 있다.")
    @CsvSource(value = {"10, 5", "1000, 0", "0, 0"})
    void 서로_다른_두_값을_뺄_수_있다(int a, int b) {
        // given
        Quantity aQuantity = new Quantity(a);
        Quantity bQuantity = new Quantity(b);

        // when
        Quantity subtracted = aQuantity.subtract(bQuantity);

        // then
        assertThat(subtracted.value()).isEqualTo(a - b);
    }

    @ParameterizedTest
    @DisplayName("[정상] 서로 다른 객체끼리 덧셈 연산을 처리할 수 있다.")
    @CsvSource(value = {"10, 5", "1000, 0", "0, 0"})
    void 서로_다른_두_값을_더할_수_있다(int a, int b) {
        // given
        Quantity aQuantity = new Quantity(a);
        Quantity bQuantity = new Quantity(b);

        // when
        Quantity summed = aQuantity.sum(bQuantity);

        // then
        assertThat(summed.value()).isEqualTo(a + b);
    }

    @ParameterizedTest
    @DisplayName("[정상] 올바른 재고 상태를 반환할 수 있다.")
    @CsvSource(value = {"0; 재고 없음", "1000; 1,000개"}, delimiter = ';')
    void 올바른_재고_메시지_출력(int value, String result) {
        // given
        Quantity quantity = new Quantity(value);

        // when
        String quantityText = quantity.toString();

        // then
        assertThat(quantityText).isEqualTo(result);
    }

    @Test
    @DisplayName("[예외] 작은 값에서 큰 값을 뺄 수 없다.")
    void 뺄셈_예외() {
        // given
        Quantity a = new Quantity(10);
        Quantity b = new Quantity(11);

        // when & then
        assertThatIllegalStateException().isThrownBy(() -> {
            a.subtract(b);
        });
    }
}