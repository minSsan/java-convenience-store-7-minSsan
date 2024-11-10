package store.infrastructure.constant;

public enum ExceptionMessage {
    WRONG_INTEGER_FORMAT("숫자 형식이 올바르지 않습니다. 다시 입력해 주세요."),
    WRONG_QUANTITY_RANGE("재고는 0 ~ 1000 사이의 숫자만 설정할 수 있습니다.")
    ;

    private final String message;

    private ExceptionMessage(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}
