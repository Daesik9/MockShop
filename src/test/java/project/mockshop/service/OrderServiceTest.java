package project.mockshop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import project.mockshop.dto.OrderDto;
import project.mockshop.entity.*;
import project.mockshop.repository.CartRepository;
import project.mockshop.repository.OrderRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @InjectMocks
    OrderService orderService;

    @Mock
    OrderRepository orderRepository;
    @Mock
    CartRepository cartRepository;

    List<Order> orders = new ArrayList<>();
    Merchant merchant1;
    Customer customer1;

    @BeforeEach
    void beforeEach() {
        merchant1 = Merchant.builder().name("merchant1").build();
        Merchant merchant2 = Merchant.builder().name("merchant2").build();

        customer1 = Customer.builder()
                .loginId("customer1")
                .name("구매자일")
                .phoneNumber("01011111111")
                .build();
        Customer customer2 = Customer.builder()
                .loginId("customer2")
                .name("구매자이")
                .phoneNumber("01022222222")
                .build();

        Item item1Merchant1 = Item.builder()
                .quantity(100)
                .merchant(merchant1)
                .name("item1")
                .build();

        Item item2Merchant1 = Item.builder()
                .quantity(100)
                .merchant(merchant1)
                .build();

        Item item3Merchant1 = Item.builder()
                .quantity(100)
                .merchant(merchant1)
                .build();

        Item item1Merchant2 = Item.builder()
                .quantity(100)
                .merchant(merchant2)
                .name("item1")
                .build();

        Item item2Merchant2 = Item.builder()
                .quantity(100)
                .merchant(merchant2)
                .build();

        Order order1 = Order.builder()
                .id(1L)
                .customer(customer1)
                .address(new Address("city", "street", "11111"))
                .paymentMethod("card")
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.ORDER)
                .orderItems(List.of(OrderItem.builder().id(1L).item(item1Merchant1).build()))
                .orderDate(LocalDateTime.now().minusDays(5))
                .build();

        Order order2 = Order.builder()
                .id(2L)
                .customer(customer2)
                .address(new Address("city", "street", "11111"))
                .paymentMethod("card")
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.ORDER)
                .orderNumber("12345")
                .orderItems(List.of(OrderItem.builder().id(2L).item(item2Merchant1).build()))
                .orderDate(LocalDateTime.now())
                .build();

        Order order3 = Order.builder()
                .id(3L)
                .customer(customer1)
                .address(new Address("city", "street", "11111"))
                .paymentMethod("card")
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.ORDER)
                .orderItems(List.of(OrderItem.builder().id(3L).item(item3Merchant1).build()))
                .orderDate(LocalDateTime.now().minusDays(4))
                .build();

        Order order4 = Order.builder()
                .id(4L)
                .customer(customer2)
                .address(new Address("city", "street", "11111"))
                .paymentMethod("card")
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.ORDER)
                .orderDate(LocalDateTime.now().minusDays(2))
                .orderItems(List.of(OrderItem.builder().id(4L).item(item1Merchant2).build()))
                .build();

        Order order5 = Order.builder()
                .id(5L)
                .customer(customer1)
                .address(new Address("city", "street", "11111"))
                .paymentMethod("card")
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.ORDER)
                .orderItems(List.of(OrderItem.builder().id(5L).item(item2Merchant2).build()))
                .orderDate(LocalDateTime.now().plusDays(5))
                .build();

        orders.add(order1);
        orders.add(order2);
        orders.add(order3);
        orders.add(order4);
        orders.add(order5);

        orderRepository.save(order1);
        orderRepository.save(order2);
        orderRepository.save(order3);
        orderRepository.save(order4);
        orderRepository.save(order5);
    }

    @Test
    void serviceNotNull() throws Exception {
        //given

        //when

        //then
        assertThat(orderService).isNotNull();
        assertThat(orderRepository).isNotNull();
    }

    @Test
    void order() throws Exception {
        //given
        Customer customer = Customer.builder().name("테스트").address(new Address("city", "street", "11111")).build();
        Item item = Item.builder().quantity(100).build();
        OrderItem orderItem = OrderItem.builder().item(item).build();
        Order order = Order.builder()
                .customer(customer)
                .orderItems(List.of(orderItem))
                .build();
        Cart cart = Cart.builder().customer(customer).cartItems(List.of(CartItem.builder().item(item).build())).build();
        given(cartRepository.findCartWithItems(customer.getId())).willReturn(cart);

        //when
        String paymentMethod = "MOCK_PAY";
        String orderNumber = orderService.order(customer.getId(), paymentMethod);
        when(orderRepository.findByOrderNumber(orderNumber)).thenReturn(Optional.of(order));

        //then
        Optional<Order> optionalOrder = orderRepository.findByOrderNumber(orderNumber);
        assertThat(optionalOrder.get().getCustomer()).isEqualTo(customer);
    }



    @Test
    void findAll() throws Exception {
        //given
        given(orderRepository.findAll()).willReturn(orders);

        //when
        List<OrderDto> orders = orderService.findAll();

        //then
        assert orders != null;
        assertThat(orders.size()).isEqualTo(5);
    }

//    @Test
//    void findAllByMerchant() throws Exception {
//        //given
//        given(orderRepository.findAllByMerchant(merchant1))
//                .willReturn(orders.subList(0, 3));
//
//        //when
//        List<OrderDto> orders = orderService.findAllByMerchant(merchant1);
//
//        //then
//        assert orders != null;
//        assertThat(orders.size()).isEqualTo(3);
//    }

    @Test
    void findAllByCustomer() throws Exception {
        //given
        given(orderRepository.findAllByCustomerId(customer1.getId()))
                .willReturn(List.of(orders.get(0), orders.get(2), orders.get(4)));

        //when
        List<OrderDto> orders = orderService.findAllByCustomerId(customer1.getId());

        //then
        assert orders != null;
        assertThat(orders.size()).isEqualTo(3);
    }

    @Test
    void findAllByStatus() throws Exception {
        //given
        given(orderRepository.findAllByStatus(OrderStatus.ORDER))
                .willReturn(List.of(orders.get(0), orders.get(2), orders.get(3)));

        //when
        List<OrderDto> orders = orderService.findAllByStatus(OrderStatus.ORDER);

        //then
        assert orders != null;
        assertThat(orders.size()).isEqualTo(3);
    }

    @Test
    void findAllByOrderDate() throws Exception {
        //given
        LocalDateTime from = LocalDateTime.now().minusDays(3);
        LocalDateTime to = LocalDateTime.now().plusDays(1);
        given(orderRepository.findAllByOrderDateBetween(from, to))
                .willReturn(List.of(orders.get(1), orders.get(3)));

        //when
        List<OrderDto> orders = orderService.findAllByOrderDate(from, to);

        //then
        assert orders != null;
        assertThat(orders.size()).isEqualTo(2);
    }

    @Test
    void findByOrderNumber() throws Exception {
        //given
        given(orderRepository.findByOrderNumber("12345")).willReturn(Optional.of(orders.get(1)));

        //when
        OrderDto order = orderService.findByOrderNumber("12345");

        //then
        assert order != null;
        assertThat(order.getStatus()).isEqualTo(OrderStatus.ORDER);
    }

    @Test
    void findAllByItemName() throws Exception {
        //given
        given(orderRepository.findAllByItemName("item1"))
                .willReturn(List.of(orders.get(0), orders.get(3)));

        //when
        List<OrderDto> orders = orderService.findAllByItemName("item1");

        //then
        assert orders != null;
        assertThat(orders.size()).isEqualTo(2);
    }

//    @Test
//    void findAllByMerchantName() throws Exception {
//        //given
//        given(orderRepository.findAllByMerchantName("merchant1"))
//                .willReturn(List.of(orders.get(0), orders.get(1), orders.get(2)));
//
//        //when
//        List<OrderDto> orders = orderService.findAllByMerchantName("merchant1");
//
//        //then
//        assert orders != null;
//        assertThat(orders.size()).isEqualTo(3);
//    }

    @Test
    void findAllByCustomerPhoneNumber() throws Exception {
        //given
        given(orderRepository.findAllByCustomerPhoneNumber("01011111111"))
                .willReturn(List.of(orders.get(0), orders.get(2), orders.get(4)));

        //when
        List<OrderDto> orders = orderService.findAllByCustomerPhoneNumber("01011111111");

        //then
        assert orders != null;
        assertThat(orders.size()).isEqualTo(3);
    }

    @Test
    void findAllByCustomerLoginId() throws Exception {
        //given
        given(orderRepository.findAllByCustomerLoginId("customer2"))
                .willReturn(List.of(orders.get(1), orders.get(3)));

        //when
        List<OrderDto> orders = orderService.findAllByCustomerLoginId("customer2");

        //then
        assert orders != null;
        assertThat(orders.size()).isEqualTo(2);
    }

    @Test
    void findAllByCustomerName() throws Exception {
        //given
        given(orderRepository.findAllByCustomerName("구매자일"))
                .willReturn(List.of(orders.get(0), orders.get(2), orders.get(4)));

        //when
        List<OrderDto> orders = orderService.findAllByCustomerName("구매자일");

        //then
        assert orders != null;
        assertThat(orders.size()).isEqualTo(3);
    }

}