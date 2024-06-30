package project.mockshop.entity;

import org.junit.jupiter.api.Test;
import project.mockshop.policy.MockShopPolicy;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

public class CartItemTest {

    @Test
    void cartItem() throws Exception {
        //given

        //when
        CartItem cartItem = CartItem.builder()
                .item(Item.builder().build())
                .cartPrice(1000)
                .count(10)
                .build();

        //then
        assertThat(cartItem.getCartPrice()).isEqualTo(1000);
    }

    @Test
    void cartItem_fail_cartPrice() throws Exception {
        //given

        //when

        //then
        assertThatThrownBy(() -> CartItem.builder()
                .item(Item.builder().build())
                .cartPrice(-100)
                .count(10)
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(MockShopPolicy.INPUT_STRING_METHOD("가격"));
    }

    @Test
    void cartItem_fail_count() throws Exception {
        //given

        //when

        //then
        assertThatThrownBy(() -> CartItem.builder()
                .item(Item.builder().build())
                .cartPrice(10000)
                .count(-10)
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(MockShopPolicy.INPUT_STRING_METHOD("수량"));
    }

}