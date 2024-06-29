package project.mockshop.entity;

import jakarta.persistence.*;
import lombok.*;
import project.mockshop.policy.MockShopPolicy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Builder
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "orders")
public class Order {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Embedded
    private Address address;

    private String paymentMethod;
    private LocalDateTime orderDate;
    private String status;

    @OneToMany//(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    private String orderNumber;

    public static class OrderBuilder {
        public OrderBuilder address(Address address) {
            if (address == null) {
                throw new IllegalArgumentException(MockShopPolicy.INPUT_STRING_METHOD("배송지"));
            }
            this.address = address;

            return this;
        }

        public OrderBuilder orderItems(OrderItem... orderItems) {
            if (orderItems.length == 0) {
                throw new IllegalStateException("주문 상품이 없습니다.");
            }
            this.orderItems = new ArrayList<>();
            this.orderItems.addAll(Arrays.asList(orderItems));
//            for (OrderItem orderItem : orderItems) {
//                orderItem.changeOrder(this);
//            }
            return this;
        }

    }

}