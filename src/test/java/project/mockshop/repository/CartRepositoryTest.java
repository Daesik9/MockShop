package project.mockshop.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import project.mockshop.entity.*;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class CartRepositoryTest {
    @Autowired
    CartRepository cartRepository;
    @Autowired
    ItemRepository itemRepository;

    @Test
    void notNull() throws Exception {
        //given

        //when

        //then
        assertThat(cartRepository).isNotNull();
    }

    @Test
    void findAll() throws Exception {
        //given
        Item item = Item.builder().build();
        itemRepository.save(item);

        CartItem cartItem = CartItem.builder().item(item).build();
        Cart cart = Cart.builder().cartItems(List.of(cartItem)).build();
        cartRepository.save(cart);

        //when
        List<Cart> carts = cartRepository.findAll();

        //then
        assertThat(carts.size()).isEqualTo(1);
    }



}