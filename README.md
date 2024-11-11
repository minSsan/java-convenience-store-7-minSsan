# java-convenience-store-precourse

### [📒 노트](https://github.com/minSsan/java-convenience-store-7-minSsan/blob/main/NOTE.md)

# 프로젝트 목표

- 기존 설계(ex. 헥사고날)가 아닌, 스스로 아키텍처를 설계하고 적용한다.

- 클래스 (도메인) 분리를 신경쓴다.

  - 스스로 분류 기준을 설정하고, 역할과 딘일 책임 원칙이 지켜지고 있는지 점검한다.

- 테스트 주도 개발을 적용한다.
  - 테스트 주도 개발을 통해 `"테스트가 쉬운 코드"`를 만들기 위해 노력한다.

# 프로젝트 설명

> 편의점 상품 결제 및 재고 관리 서비스를 제공합니다.

# 설명서

> 작성 예정

# 기능 요구사항

## 입력

> 사용자 입력을 처리하는 기능 요구사항 리스트

- [x] 파일 입력
  - [x] 상품 목록 (`src/main/resources/products.md`)
  - [x] 행사 목록 (`src/main/resources/promotions.md`)
- [x] 구매 상품, 수량 입력  
       👉🏻 상품명, 수량은 하이픈(`-`)으로, 개별 상품은 대괄호(`[]`)로 묶어 쉼표(`,`)로 구분
  ```
  [콜라-10],[사이다-3]
  ```
- [x] 프로모션 적용이 가능한 상품에 대해, 고객이 해당 수량보다 적게 가져온 경우

  ```
  현재 {상품명}은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)
  ```

  - [x] `Y`: 증정 받을 수 있는 상품을 추가한다.
  - [x] `N`: 증정 받을 수 있는 상품을 추가하지 않는다.

- [x] 프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우

  ```
  현재 {상품명} {수량}개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)
  ```

  - [x] `Y`: 일부 수량에 대해 정가로 결제한다.
  - [x] `N`: 정가로 결제해야하는 수량만큼 제외한 후 결제를 진행한다.

- [x] 멤버십 할인 적용 여부를 입력 받는다.

  ```
  멤버십 할인을 받으시겠습니까? (Y/N)
  ```

  - [x] `Y`: 멤버십 할인을 적용한다.
  - [x] `N`: 멤버십 할인을 적용하지 않는다.

- [x] 추가 구매 여부를 입력 받는다.
  ```
  감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)
  ```
  - [x] `Y`: 재고가 업데이트된 상품 목록을 확인 후 추가로 구매를 진행한다.
  - [x] `N`: 구매를 종료한다.

## 안내 출력

> 사용자에게 안내문을 출력하는 기능 요구사항 리스트

- [x] 환영 인사와 함께 `상품명`, `가격`, `프로모션 이름`, `재고`를 안내
  - [x] 재고가 0일 경우 `"재고 없음"` 출력
- [x] 사용자가 잘못된 값을 입력했을 때, `"[ERROR]"`로 시작하는 오류 메시지와 함께 상황에 맞는 안내를 출력한다.

  - [x] 구매할 상품과 수량 형식이 올바르지 않은 경우

    ```
    [ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.
    ```

  - [x] 존재하지 않는 상품을 입력한 경우
    ```
    [ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.
    ```
  - [x] 구매 수량이 재고 수량을 초과한 경우
    ```
    [ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.
    ```
  - [x] 기타 잘못된 입력의 경우
    ```
    [ERROR] 잘못된 입력입니다. 다시 입력해 주세요.
    ```

#### 예시

```
안녕하세요. W편의점입니다.
현재 보유하고 있는 상품입니다.

- 콜라 1,000원 10개 탄산2+1
- 콜라 1,000원 10개
- 사이다 1,000원 8개 탄산2+1
- 사이다 1,000원 7개
- 오렌지주스 1,800원 9개 MD추천상품
- 오렌지주스 1,800원 재고 없음
- 탄산수 1,200원 5개 탄산2+1
- 탄산수 1,200원 재고 없음
- 물 500원 10개
- 비타민워터 1,500원 6개
- 감자칩 1,500원 5개 반짝할인
- 감자칩 1,500원 5개
- 초코바 1,200원 5개 MD추천상품
- 초코바 1,200원 5개
- 에너지바 2,000원 5개
- 정식도시락 6,400원 8개
- 컵라면 1,700원 1개 MD추천상품
- 컵라면 1,700원 10개

구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])
```

## 결과 출력

> 서비스 실행 결과를 출력하는 요구사항 리스트

- [x] 구매 내역, 산출 금액 정보

## 기능

> 서비스 내부 기능 _(= 비즈니스 로직)_ 에 대한 요구사항 리스트

### 실행 흐름 관리

- [x] 영수증 출력 후, 추가구매 진행 / 종료 여부 결정

### 계산

- [x] 총 구매액 계산 (상품별 가격 X 수량)
- [x] 최종 결제 금액 계산 _(👉🏻 프로모션 및 멤버십 할인 적용 금액)_

### 재고 관리

- [x] 수량 관리
  - [x] 상품 구매시 결제 수량만큼 재고 차감  
         👉🏻 `최신 재고 상태`를 유지하여 정확한 재고 정보 제공

### 프로모션 할인

- [x] 오늘 날짜가 프로모션 기간에 해당하는 경우에만 적용
- [x] N개 구매시 1개 증정 (`N+1`) 형태로 적용 _(❗️동일 상품에 여러 프로모션 적용 X❗️)_
  - [x] `1+1` / `2+1` 프로모션이 각각 지정된 상품에 적용
- [x] 프로모션 재고 내에서만 적용 가능
- [x] 프로모션 기간
  - [x] 프로모션 재고 우선 차감
  - [x] 프로모션 재고 부족할 경우, 일반 재고 차감

### 프로모션 출력

- [x] 프로모션 적용 상품에 대해, 프로모션 수량보다 적게 가져온 경우  
       👉🏻 필요 수량을 추가로 가져오면 혜택을 받을 수 있음을 안내
- [x] 프로모션 재고가 부족한 경우
  - [x] 일부 수량을 프로모션 혜택 없이 결재
  - [x] 일부 수량에 대해 정가로 결제하게 됨을 안내

### 멤버십 할인

- [x] 멤버십 회원은 `프로모션 미적용 금액의 30%` 할인
- [x] 프로모션 적용 후 남은 금액에 대해 멤버십 할인 적용
- [x] 멤버십 할인 `최대 한도`는 `8,000원`

### 영수증 출력

- [x] 구매 내역 (상품명, 수량, 가격)
- [x] 증정 상품 내역 _(👉🏻 프로모션에 따라 무료로 제공된 증정 상품 목록)_
- [x] 금액 정보
  - [x] 총 금액
  - [x] 행사 할인 _(👉🏻 프로모션에 의해 할인된 금액)_
  - [x] 멤버십 할인 _(👉🏻 멤버십에 의해 추가 할인된 금액)_
  - [x] 내실 돈 _(👉🏻 최종 결제 금액)_
- [x] 필드 칸 정렬

#### 예시

```text
===========W 편의점=============
상품명		수량	금액
콜라		3 	3,000
에너지바 		5 	10,000
===========증	정=============
콜라		1
==============================
총구매액		8	13,000
행사할인			-1,000
멤버십할인			-3,000
내실돈			 9,000
```

## 예외

> 문제에 제시된 조건 이외에 고려할 수 있는 예외

## 추가 예외

> 스스로 설정한 추가 예외 상황

- [x] 같은 제품을 여러번 입력한 경우 ex) `[콜라-1],[콜라-5]`
  - [x] 서로 다른 입력을 합치기 ex) `[콜라-6]`
