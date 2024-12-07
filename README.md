# kotlin-convenience-store-precourse

## 리팩토링 사항

- [ ] BuyController가 하던 일을 분리하기


점점 코드가 남이 짠 것처럼 기억이 안나네요 😂

```

### 순서도

요구사항이 복잡하여 나름대로 순서도를 그려 정리하였다

![image](https://github.com/user-attachments/assets/4f567c50-2fe5-49aa-abe7-76168a8db023)

### 기능목록

- 상품
    - [x] 구매한 개수만큼 상품의 수량이 줄어든다
    - [x] 구매할 수 있는지 확인한다
    - [x] 재고 수량보다 많은 수량을 구매하면, 예외가 발생한다

- 프로모션
    - [x] 프로모션 기간인경우에만 할인을 적용한다.
    - [x] 프로모션 n개 구매시 1개 무료 증정
    - [x] 프로모션 상품이 소진되면 일반 재고를 사용한다.
    - [x] 고객에 프로모션 증정을 받을 수 있는 경우 추가할건지 물어본다
    - [x] 재고가 부족하여 혜택없이 결제해야하는 경우, 일부 수량에 대해 정가로 결제해도 되는지 물어본다
    - [x] 프로모션 재고를 우선적으로 차감한다.

- 멤버쉽
    - [x] 멤버쉽 회원은 프로모션이 아닌 물품에 대해 30% 할인한다
    - [x] 멤버십 할인 최대 한도는 8000다

- 영수증
    - [x] 구매한 상품명, 수량, 가격
    - [x] 무료 증성 상품
    - [x] 총 구매액, 행사할인금액, 멤버십 할인금액, 최종 내야할 금액
```