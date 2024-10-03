package project.mockshop.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.mockshop.dto.OrderDto;
import project.mockshop.entity.*;
import project.mockshop.repository.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
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
    @Autowired
    MemberRepository memberRepository;


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

    @Test
    void findAllByMerchantId() throws Exception {
        //given
        Merchant merchant1 = Merchant.builder().name("merchant1").build();
        Merchant merchant2 = Merchant.builder().name("merchant2").build();

        Customer customer = Customer.builder()
                .loginId("customer1")
                .name("구매자일")
                .phoneNumber("01011111111")
                .build();

        Item item1 = Item.builder()
                .quantity(100)
                .merchant(merchant1)
                .name("item1")
                .build();

        Item item2 = Item.builder()
                .quantity(100)
                .merchant(merchant1)
                .build();

        Item item3 = Item.builder()
                .quantity(100)
                .merchant(merchant1)
                .build();

        Item item4 = Item.builder()
                .quantity(100)
                .merchant(merchant2)
                .build();

        Order order1 = Order.builder()
                .id(1L)
                .customer(customer)
                .address(new Address("city", "street", "11111"))
                .paymentMethod("card")
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.ORDER)
                .orderItems(List.of(OrderItem.builder().id(1L).item(item1).build()))
                .orderDate(LocalDateTime.now().minusDays(5))
                .build();

        Order order2 = Order.builder()
                .id(2L)
                .customer(customer)
                .address(new Address("city", "street", "11111"))
                .paymentMethod("card")
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.ORDER)
                .orderNumber("12345")
                .orderItems(List.of(OrderItem.builder().id(2L).item(item2).build()))
                .orderDate(LocalDateTime.now())
                .build();

        Order order3 = Order.builder()
                .id(3L)
                .customer(customer)
                .address(new Address("city", "street", "11111"))
                .paymentMethod("card")
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.ORDER)
                .orderItems(List.of(OrderItem.builder().id(3L).item(item3).build()))
                .orderDate(LocalDateTime.now().minusDays(4))
                .build();

        Order order4 = Order.builder()
                .id(4L)
                .customer(customer)
                .address(new Address("city", "street", "11111"))
                .paymentMethod("card")
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.ORDER)
                .orderDate(LocalDateTime.now().minusDays(2))
                .orderItems(List.of(OrderItem.builder().id(4L).item(item4).build()))
                .build();

        memberRepository.save(merchant1);
        memberRepository.save(merchant2);
        memberRepository.save(customer);

        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);
        itemRepository.save(item4);

        orderRepository.save(order1);
        orderRepository.save(order2);
        orderRepository.save(order3);
        orderRepository.save(order4);

        //when
        List<OrderDto> ordersByMerchantId = orderService.findAllByMerchantId(merchant1.getId());

        //then
        assertThat(ordersByMerchantId.size()).isEqualTo(3);
    }

}