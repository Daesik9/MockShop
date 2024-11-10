package project.mockshop.entity;

import org.junit.jupiter.api.Test;
import project.mockshop.policy.MockShopPolicy;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class OrderTest {

    @Test
    void order() throws Exception {
        //given
        Item item = Item.builder()
                .quantity(100)
                .build();
        Order order = Order.builder()
                .customer(Customer.builder().build())
                .addressInfo(new AddressInfo("city", "street", "11111"))
                .paymentMethod("card")
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.ORDER)
                .orderItems(List.of(OrderItem.builder().item(item).build()))
                .build();

        //when

        //then
        assertThat(order.getPaymentMethod()).isEqualTo("card");
    }
    
    @Test
    void order_fail_orderItemsEmpty() throws Exception {
        //given
        
        //when
        
        //then
        assertThatThrownBy(() -> Order.builder()
                .customer(Customer.builder().build())
                .addressInfo(new AddressInfo("city", "street", "11111"))
                .paymentMethod("card")
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.ORDER)
                .orderItems(List.of())
                .build())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("주문 상품이 없습니다.");

    }

    @Test
    void order_fail_addressEmpty() throws Exception {
        //given
        Item item = Item.builder()
                .quantity(100)
                .build();

        //when

        //then
        assertThatThrownBy(() -> Order.builder()
                .customer(Customer.builder().build())
                .addressInfo(null)
                .paymentMethod("card")
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.ORDER)
                .orderItems(List.of(OrderItem.builder().item(item).build()))
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(MockShopPolicy.INPUT_STRING_METHOD("배송지"));
    }

    @Test
    void order_discountAmount() throws Exception {
        //given
        Item item = Item.builder()
                .quantity(100)
                .build();
        Order order = Order.builder()
                .customer(Customer.builder().build())
                .addressInfo(new AddressInfo("city", "street", "11111"))
                .paymentMethod("card")
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.ORDER)
                .orderItems(List.of(OrderItem.builder().item(item).build()))
                .discountAmount(3000)
                .build();

        //when

        //then
        assertThat(order.getDiscountAmount()).isEqualTo(3000);
    }

}
