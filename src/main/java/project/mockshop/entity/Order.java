package project.mockshop.entity;

import jakarta.persistence.*;
import lombok.*;
import project.mockshop.policy.MockShopPolicy;
import project.mockshop.util.OrderNumberGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private AddressInfo addressInfo;

    private String paymentMethod;
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;

    private String orderNumber;

    public static Order createOrder(Customer customer, String paymentMethod, List<OrderItem> orderItems) {
        return Order.builder()
                .customer(customer)
                .addressInfo(customer.getAddressInfo())
                .paymentMethod(paymentMethod)
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.ORDER)
                .orderItems(orderItems)
                .orderNumber(OrderNumberGenerator.generateOrdeNumber())
                .build();
    }

    public static class OrderBuilder {
            public OrderBuilder addressInfo(AddressInfo addressInfo) {
            if (addressInfo == null) {
                throw new IllegalArgumentException(MockShopPolicy.INPUT_STRING_METHOD("배송지"));
            }
            this.addressInfo = addressInfo;

            return this;
        }

        public OrderBuilder orderItems(List<OrderItem> orderItems) {
            if (orderItems.size() == 0) {
                throw new IllegalStateException("주문 상품이 없습니다.");
            }
            this.orderItems = new ArrayList<>();
            this.orderItems.addAll(orderItems);
            return this;
        }

        public Order build() {
            Order order = new Order(id, customer, addressInfo, paymentMethod, orderDate, status, orderItems, orderNumber);

            for (OrderItem orderItem : this.orderItems) {
                orderItem.changeOrder(order);
            }
            return order;
        }

    }
    


}