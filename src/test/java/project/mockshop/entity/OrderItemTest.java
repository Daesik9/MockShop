package project.mockshop.entity;

import org.junit.jupiter.api.Test;
import project.mockshop.policy.MockShopPolicy;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

public class OrderItemTest {
    @Test
    void orderItem() throws Exception {
        //given
        OrderItem orderItem = OrderItem.builder()
                .id(1L)
                .item(Item.builder().quantity(10).build())
//                .order(Order.builder().build())
                .orderPrice(1000)
                .count(10)
                .build();

        //when

        //then
        assertThat(orderItem.getOrderPrice()).isEqualTo(1000);
    }

    @Test
    void orderItem_fail_minusOrderPrice() throws Exception {
        //given

        //when
        assertThatThrownBy(() -> OrderItem.builder()
                .id(1L)
                .item(Item.builder().build())
//                .order(Order.builder().build())
                .orderPrice(-1000)
                .count(10)
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(MockShopPolicy.INPUT_STRING_METHOD("주문 가격"));

        //then

    }

    @Test
    void orderItem_fail_minusCount() throws Exception {
        //given

        //when
        assertThatThrownBy(() -> OrderItem.builder()
                .id(1L)
                .item(Item.builder().build())
//                .order(Order.builder().build())
                .orderPrice(1000)
                .count(-10)
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(MockShopPolicy.INPUT_STRING_METHOD("주문 수량"));

        //then

    }

    @Test
    void orderItem_fail_countGtItemQuantity() throws Exception {
        //given

        //when
        assertThatThrownBy(() -> OrderItem.builder()
                .id(1L)
                .item(Item.builder().quantity(100).build())
//                .order(Order.builder().build())
                .orderPrice(1000)
                .count(101)
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문 수량이 현재 재고보다 많습니다.");

        //then
    }
}