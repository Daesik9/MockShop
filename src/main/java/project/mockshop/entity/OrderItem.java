package project.mockshop.entity;

import jakarta.persistence.*;
import lombok.*;
import project.mockshop.policy.MockShopPolicy;

@Builder
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OrderItem {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    // OrderItem에서 order에 접근할 필요가 있나?
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;
    private int count;

    public static OrderItem createOrderItem(Item item, int cartPrice, int count) {
        return OrderItem.builder()
                .item(item)
                .orderPrice(cartPrice)
                .count(count)
                .build();
    }

    public void changeOrder(Order order) {
        this.order = order;
    }

    public static class OrderItemBuilder {
        public OrderItemBuilder orderPrice(int orderPrice) {
            if (orderPrice < 0) {
                throw new IllegalArgumentException(MockShopPolicy.INPUT_STRING_METHOD("주문 가격"));
            }
            this.orderPrice = orderPrice;
            return this;
        }

        public OrderItemBuilder count(int count) {
            if (count < 0) {
                throw new IllegalArgumentException(MockShopPolicy.INPUT_STRING_METHOD("주문 수량"));
            }
            this.count = count;
            return this;
        }

        public OrderItem build() {
            if (count > item.getQuantity()) {
                throw new IllegalArgumentException("주문 수량이 현재 재고보다 많습니다.");
            }
//            return new OrderItem(id, item, order, orderPrice, count);
            return new OrderItem(id, item, order, orderPrice, count);
        }
    }

}