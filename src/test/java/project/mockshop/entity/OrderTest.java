package project.mockshop.entity;

import org.junit.jupiter.api.Test;
import project.mockshop.policy.MockShopPolicy;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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
                .address(new Address("city", "street", "11111"))
                .paymentMethod("card")
                .orderDate(LocalDateTime.now())
                .status("order")
                .orderItems(OrderItem.builder().item(item).build())
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
                .address(new Address("city", "street", "11111"))
                .paymentMethod("card")
                .orderDate(LocalDateTime.now())
                .status("order")
                .orderItems()
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
                .address(null)
                .paymentMethod("card")
                .orderDate(LocalDateTime.now())
                .status("order")
                .orderItems(OrderItem.builder().item(item).build())
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(MockShopPolicy.INPUT_STRING_METHOD("배송지"));
    }
}