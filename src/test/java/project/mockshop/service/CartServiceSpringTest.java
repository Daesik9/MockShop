package project.mockshop.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import project.mockshop.entity.Cart;
import project.mockshop.entity.CartItem;
import project.mockshop.entity.Customer;
import project.mockshop.entity.Item;
import project.mockshop.repository.CartRepository;
import project.mockshop.repository.CustomerRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class CartServiceSpringTest {
    @Autowired
    CartService cartService;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    CustomerRepository customerRepository;

    Cart cart;
    Customer customer;

    @BeforeEach
    void beforeEach() {
        customer = Customer.builder().build();
        customerRepository.save(customer);

        cart = Cart.builder()
                .cartItems(List.of(CartItem.builder().count(1).build()))
                .customer(customer)
                .build();

        cartRepository.save(cart);
    }

    @Test
    void changeCartItemCount() throws Exception {
        //given
        Cart cart = Cart.builder().cartItems(List.of(CartItem.builder().item(Item.builder().quantity(10).build()).count(1).build())).build();

        cartRepository.save(cart);

        //when
        int count = 3;
        cartService.changeCartItemCount(cart.getId(), cart.getCartItems().get(0).getId(), count);

        //then
        assertThat(cart.getCartItems().get(0).getCount()).isEqualTo(3);
    }

    @Test
    void changeCartItemCount_fail() throws Exception {
        //given
        Cart cart = Cart.builder()
                .cartItems(List.of(CartItem.builder()
                        .item(Item.builder()
                                .quantity(10)
                                .build())
                        .count(1)
                        .build()))
                .build();

        cartRepository.save(cart);

        //when
        int count = 20;
        assertThatThrownBy(() -> cartService.changeCartItemCount(cart.getId(), cart.getCartItems().get(0).getId(), count))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("재고가 부족합니다.");

        //then
    }

    @Test
    void removeCartItem() throws Exception {
        //given

        //when
        cartService.removeCartItem(cart.getId(), 1L);

        //then
        assertThat(cart.getCartItems().size()).isEqualTo(0);
    }

    @Test
    void getCartItems() throws Exception {
        //given

        //when
        Cart findCart = cartRepository.findCartWithItems(customer.getId());

        //then
        assertThat(findCart.getCartItems().get(0).getCount()).isEqualTo(1);
    }

}