package project.mockshop.entity;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class CartTest {

    @Test
    void cart() throws Exception {
        //given

        //when
        Cart cart = Cart.builder()
                .customer(Customer.builder().name("구매자").build())
                .cartItems(List.of())
//                .cartItems(CartItem.builder().build())
                .build();

        //then
        assertThat(cart.getCustomer().getName()).isEqualTo("구매자");
        assertThat(cart.getCartItems().size()).isEqualTo(0);
    }


}