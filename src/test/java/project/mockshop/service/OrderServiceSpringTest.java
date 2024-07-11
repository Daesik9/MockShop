package project.mockshop.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.mockshop.entity.*;
import project.mockshop.repository.CartRepository;
import project.mockshop.repository.CustomerRepository;
import project.mockshop.repository.ItemRepository;
import project.mockshop.repository.OrderRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
public class OrderServiceSpringTest {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    ItemRepository itemRepository;


    @Test
    void order() throws Exception {
        //given
        Customer customer = Customer.builder().name("테스트").address(new Address("city", "street", "11111")).build();
        customerRepository.save(customer);

        Item item = Item.builder().name("사과").quantity(100).price(1000).build();
        itemRepository.save(item);

        CartItem cartItem = CartItem.builder().item(item).build();
        Cart cart = Cart.builder().cartItems(List.of(cartItem)).customer(customer).build();
        cartRepository.save(cart);

        //when
        String paymentMethod = "MOCK_PAY";
        String orderNumber = orderService.order(customer.getId(), paymentMethod);


        //then
        Order foundOrder = orderRepository.findByOrderNumber(orderNumber).orElseThrow(() -> new NullPointerException("해당 주문이 없습니다."));
        assertThat(foundOrder.getCustomer()).isEqualTo(customer);
        assertThat(foundOrder.getOrderItems().size()).isEqualTo(1);
        assertThat(foundOrder.getStatus()).isEqualTo(OrderStatus.ORDER);
        assertThat(foundOrder.getOrderItems().get(0).getOrder()).isNotNull();
    }
}