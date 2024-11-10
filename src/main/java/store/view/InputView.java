package store.view;

import camp.nextstep.edu.missionutils.Console;
import store.infrastructure.constant.ExceptionMessage;
import store.infrastructure.exception.CustomException;

public class InputView {
    private static final String YES = "Y";
    private static final String NO = "N";

    public String readItem() {
        System.out.println("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
        return Console.readLine();
    }

    private boolean convertAnswerToBool(String answer) {
        if (answer.trim().equals(YES)) {
            return true;
        }
        if (answer.trim().equals(NO)) {
            return false;
        }
        throw new CustomException(ExceptionMessage.WRONG_INPUT_FORMAT.message());
    }

    public boolean readMembershipAnswer() {
        return readYesOrNoAnswer("멤버십 할인을 받으시겠습니까? (Y/N)");
    }

    /**
     * 리턴 값이 true 일 경우, 제품을 구입 목록에 추가한다.
     * @param name
     * @return
     */
    public boolean readAddFreeAnswer(String name) {
        return readYesOrNoAnswer(String.format("현재 %s은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)", name));
    }

    /**
     * 리턴 값이 false 일 경우, 프로모션이 적용되지 않는 개수를 구입 목록에서 제외한다.
     * @param name
     * @param count
     * @return
     */
    public boolean readPaidRegularPriceAnswer(String name, int count) {
        return readYesOrNoAnswer(
                String.format("현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)", name, count)
        );
    }

    public boolean readAdditionalPurchase() {
        return readYesOrNoAnswer("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)");
    }

    private boolean readYesOrNoAnswer(String message) {
        System.out.println(message);
        while (true) {
            String input = Console.readLine();
            try {
                return convertAnswerToBool(input);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
