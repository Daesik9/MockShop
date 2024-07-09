package project.mockshop.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import project.mockshop.entity.Cart;
import project.mockshop.entity.CartItem;
import project.mockshop.entity.Item;
import project.mockshop.repository.CartRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @InjectMocks
    CartService cartService;

    @Mock
    CartRepository cartRepository;

    @Test
    void notNull() throws Exception {
        //given

        //when

        //then
        assertThat(cartService).isNotNull();
        assertThat(cartRepository).isNotNull();
    }

    @Test
    void changeCartItemCount() throws Exception {
        //given
        Cart cart = Cart.builder().cartItems(List.of(CartItem.builder().id(1L).item(Item.builder().quantity(10).build()).count(1).build())).build();

        cartRepository.save(cart);
        given(cartRepository.findById(cart.getId())).willReturn(Optional.of(cart));

        //when
        int count = 3;
        cartService.changeCartItemCount(cart.getId(), 1L, count);

        //then
        assertThat(cart.getCartItems().get(0).getCount()).isEqualTo(3);
    }

    @Test
    void changeCartItemCount_fail() throws Exception {
        //given
        Cart cart = Cart.builder()
                .cartItems(List.of(CartItem.builder()
                        .id(1L)
                        .item(Item.builder()
                                .quantity(10)
                                .build())
                        .count(1)
                        .build()))
                .build();

        cartRepository.save(cart);
        given(cartRepository.findById(cart.getId())).willReturn(Optional.of(cart));

        //when
        int count = 20;
        assertThatThrownBy(() -> cartService.changeCartItemCount(cart.getId(), 1L, count))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("재고가 부족합니다.");

        //then
    }

    @Test
    void removeCartItem() throws Exception {
        //given
        Cart cart = Cart.builder().cartItems(List.of(CartItem.builder().id(1L).count(1).build())).build();
        cartRepository.save(cart);
        given(cartRepository.findById(cart.getId())).willReturn(Optional.of(cart));

        //when
        cartService.removeCartItem(cart.getId(), 1L);

        //then
        assertThat(cart.getCartItems().size()).isEqualTo(0);
    }

    @Test
    void getCartItems() throws Exception {
        //given
        Long customerId = 1L;
        Cart cart = Cart.builder().cartItems(List.of(CartItem.builder().id(1L).count(1).build())).build();
        given(cartRepository.findCartWithItems(customerId)).willReturn(cart);

        //when
        Cart findCart = cartRepository.findCartWithItems(customerId);

        //then
        assertThat(findCart.getCartItems().get(0).getCount()).isEqualTo(1);
    }

}