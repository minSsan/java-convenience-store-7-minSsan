## 🧬 프로젝트 구조

> 이번 과제에서는 **계층형 구조**로 설계하여 진행했습니다.

```
📦store
 ┣ 📂controller
 ┃ ┗ 📜StoreController.java
 ┣ 📂converter
 ┣ 📂domain
 ┃ ┣ 📂vo
 ┃ ┃ ┣ 📜Inventory.java
 ┃ ┃ ┣ 📜Name.java
 ┃ ┃ ┣ 📜Order.java
 ┃ ┃ ┣ 📜Price.java
 ┃ ┃ ┣ 📜Product.java
 ┃ ┃ ┣ 📜PromotionOption.java
 ┃ ┃ ┣ 📜PromotionQueryResult.java
 ┃ ┃ ┗ 📜Quantity.java
 ┃ ┣ 📜DiscountInfo.java
 ┃ ┣ 📜ProductInfo.java
 ┃ ┗ 📜Promotion.java
 ┣ 📂infrastructure
 ┃ ┣ 📂config
 ┃ ┃ ┗ 📜AppConfig.java
 ┃ ┣ 📂constant
 ┃ ┃ ┣ 📜Delimiter.java
 ┃ ┃ ┣ 📜ExceptionMessage.java
 ┃ ┃ ┣ 📜Membership.java
 ┃ ┃ ┣ 📜Message.java
 ┃ ┃ ┗ 📜ValueLength.java
 ┃ ┗ 📂exception
 ┃ ┃ ┗ 📜CustomException.java
 ┣ 📂repository
 ┃ ┣ 📂inventory
 ┃ ┃ ┣ 📜InventoryRepository.java
 ┃ ┃ ┗ 📜InventoryRepositoryImpl.java
 ┃ ┣ 📂order
 ┃ ┃ ┣ 📜OrderRepository.java
 ┃ ┃ ┗ 📜OrderRepositoryImpl.java
 ┃ ┣ 📂product
 ┃ ┃ ┣ 📜ProductRepository.java
 ┃ ┃ ┗ 📜ProductRepositoryImpl.java
 ┃ ┗ 📂promotion
 ┃ ┃ ┣ 📜PromotionRepository.java
 ┃ ┃ ┗ 📜PromotionRepositoryImpl.java
 ┣ 📂service
 ┃ ┣ 📂dto
 ┃ ┃ ┣ 📂request
 ┃ ┃ ┃ ┗ 📜ApplyPromotionRequest.java
 ┃ ┃ ┗ 📂response
 ┃ ┃ ┃ ┣ 📜PromotionCommandResponse.java
 ┃ ┃ ┃ ┗ 📜PromotionQueryResponse.java
 ┃ ┣ 📂strategy
 ┃ ┃ ┣ 📂provider
 ┃ ┃ ┃ ┗ 📜PromotionStrategyProvider.java
 ┃ ┃ ┣ 📜AddFreePromotionStrategy.java
 ┃ ┃ ┣ 📜ExcludeNonApplicablePromotionStrategy.java
 ┃ ┃ ┣ 📜ImmutableOrderPromotionStrategy.java
 ┃ ┃ ┗ 📜PromotionStrategy.java
 ┃ ┣ 📂vo
 ┃ ┃ ┣ 📜ProductTitle.java
 ┃ ┃ ┗ 📜PromotionTitle.java
 ┃ ┣ 📜OrderParser.java
 ┃ ┣ 📜ProductService.java
 ┃ ┣ 📜PromotionService.java
 ┃ ┣ 📜ReceiptService.java
 ┃ ┗ 📜StoreFileInputService.java
 ┣ 📂validator
 ┃ ┗ 📜OrderValidator.java
 ┣ 📂view
 ┃ ┣ 📂dto
 ┃ ┃ ┣ 📂calculate
 ┃ ┃ ┃ ┗ 📜ReceiptCalculateRequest.java
 ┃ ┃ ┣ 📂gift
 ┃ ┃ ┃ ┣ 📜ReceiptGiftDto.java
 ┃ ┃ ┃ ┗ 📜ReceiptGiftViewRequest.java
 ┃ ┃ ┣ 📂order
 ┃ ┃ ┃ ┣ 📜ReceiptOrderDto.java
 ┃ ┃ ┃ ┗ 📜ReceiptOrderViewRequest.java
 ┃ ┃ ┗ 📂product
 ┃ ┃ ┃ ┣ 📜ProductInfoDto.java
 ┃ ┃ ┃ ┗ 📜ProductViewRequest.java
 ┃ ┣ 📜InputView.java
 ┃ ┗ 📜OutputView.java
 ┗ 📜Application.java
```

## 📝 이번 과제에서 고민한 부분

### 프로모션 적용 로직

> 요구사항에 따르면, 사용자 입력 값에 따라서 **서로 다른 프로모션 적용 로직**을 실행해야 합니다.
>
> 이 과정을 `Query` _(적용 결과 조회)_ 와 `Command` _(적용 및 데이터 반영)_ 로 분류했습니다.

- 프로모션 적용 과정은 크게 네 가지로 분류할 수 있습니다.

  1. 🟧 무료 프로모션 제품을 추가하는 경우
  2. 🟩 무료 프로모션 제품을 추가하지 않고 그대로 주문하는 경우
  3. 🟩 프로모션 미적용 제품을 정가로 주문하는 경우
  4. 🟦 프로모션 미적용 제품을 주문 목록에서 제외하는 경우

     👉🏻 여기서 저는 2번 3번을 동일한 로직으로 판단했습니다.  
     👉🏻 그리고 프로모션 적용 결과는 아래와 같이 `변경된 주문 내역`, `증정 수량`으로 설정했습니다.

     ```java
     public record PromotionCommandResponse(
           Order order,
           Quantity giftQuantity
     ) {}
     ```

- 하지만 위의 3-4가지 로직을 서로 다른 클래스로 생성해서 관리하는 것은 유지보수 측면에서도 좋지 않다는 생각이 들어서 `전략패턴`을 도입했습니다.

  - **프로모션 적용 전략 일반화**

    ```java
    public interface PromotionStrategy {

        PromotionCommandResponse apply(Order order, Inventory inventory, Promotion promotion);
    }
    ```

  - **프로모션 전략 패턴 정의**

    - `사용자 선택 옵션`

    ```java
    public enum PromotionOption {
        ADD_FREE(),
        REGULAR_PURCHASE(),
        NONE(),
        ;
    }
    ```

    - `사용자 선택 옵션` 및 `사용자 응답 값`에 따른 프로모션 전략 제공자

    ```java
    public enum PromotionStrategyProvider {
        ADD_FREE_AGREE(PromotionOption.ADD_FREE, true, new AddFreePromotionStrategy()),
        ADD_FREE_DISAGREE(PromotionOption.ADD_FREE, false, new ImmutableOrderPromotionStrategy()),
        REGULAR_PURCHASE_AGREE(PromotionOption.REGULAR_PURCHASE, true, new ImmutableOrderPromotionStrategy()),
        REGULAR_PURCHASE_DISAGREE(
                PromotionOption.REGULAR_PURCHASE,
                false,
                new ExcludeNonApplicablePromotionStrategy()
        );

        private final PromotionOption option;
        private final boolean answer;
        private final PromotionStrategy strategy;

        private PromotionStrategyProvider(PromotionOption option, boolean answer, PromotionStrategy strategy) {
            this.option = option;
            this.answer = answer;
            this.strategy = strategy;
        }

        public static PromotionStrategy from(PromotionOption option, boolean answer) {
            // ...
        }
    }
    ```

### 상품 데이터 관리 - 레파지토리 계층 추가

#### 레파지토리

> `시스템은 최신 재고 상태를 유지해야 한다`는 요구사항과, **상품의 가격 정보** 및 **재고 정보**를 런타임에 영구적으로 저장하고 조회해야 합니다.
>
> 이 정보(도메인 객체)들을 `서비스 계층`에서 공유하고 관리할 수 있는 레파지토리 계층을 추가했습니다.
>
> 레파지토리 계층을 추가함으로써 **도메인 로직과 도메인 접근 로직을 서로 분리**했습니다.

- `ProductRepository`

  - 제품 정보 저장 / 조회
    - 제품명
    - 제품 가격
    - 프로모션 정보

- `OrderRepository`

  - 주문 정보 저장 / 조회
    - 주문 제품명
    - 주문량

- `InventoryRepository`
  - 재고 정보 저장 / 조회
    - 판매 제품명
    - 제품 재고량 _(프로모션 재고, 일반 재고)_
