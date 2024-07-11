package project.mockshop.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import project.mockshop.entity.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class OrderRepositoryTest {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    OrderItemRepository orderItemRepository;

    @Test
    void repositoryNotNull() throws Exception {
        //given

        //when

        //then
        assertThat(orderRepository).isNotNull();
    }


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

        memberRepository.save(merchant1);
        memberRepository.save(merchant2);
        memberRepository.save(customer1);
        memberRepository.save(customer2);

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

        itemRepository.save(item1Merchant1);
        itemRepository.save(item2Merchant1);
        itemRepository.save(item3Merchant1);
        itemRepository.save(item1Merchant2);
        itemRepository.save(item2Merchant2);

        OrderItem orderItem1 = OrderItem.builder().item(item1Merchant1).build();
        Order order1 = Order.builder()
                .customer(customer1)
                .address(new Address("city", "street", "11111"))
                .paymentMethod("card")
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.ORDER)
                .orderItems(List.of(orderItem1))
                .orderDate(LocalDateTime.now().minusDays(5))
                .build();

        OrderItem orderItem2 = OrderItem.builder().item(item2Merchant1).build();
        Order order2 = Order.builder()
                .customer(customer2)
                .address(new Address("city", "street", "11111"))
                .paymentMethod("card")
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.DELIVERED)
                .orderNumber("12345")
                .orderItems(List.of(orderItem2))
                .orderDate(LocalDateTime.now())
                .build();

        OrderItem orderItem3 = OrderItem.builder().item(item3Merchant1).build();
        Order order3 = Order.builder()
                .customer(customer1)
                .address(new Address("city", "street", "11111"))
                .paymentMethod("card")
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.ORDER)
                .orderItems(List.of(orderItem3))
                .orderDate(LocalDateTime.now().minusDays(4))
                .build();

        OrderItem orderItem4 = OrderItem.builder().item(item1Merchant2).build();
        Order order4 = Order.builder()
                .customer(customer2)
                .address(new Address("city", "street", "11111"))
                .paymentMethod("card")
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.DELIVERED)
                .orderDate(LocalDateTime.now().minusDays(2))
                .orderItems(List.of(orderItem4))
                .build();

        OrderItem orderItem5 = OrderItem.builder().item(item2Merchant2).build();
        Order order5 = Order.builder()
                .customer(customer1)
                .address(new Address("city", "street", "11111"))
                .paymentMethod("card")
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.ORDER)
                .orderItems(List.of(orderItem5))
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
    void findAll() throws Exception {
        //given

        //when
        List<Order> orders = orderRepository.findAll();

        //then
        assertThat(orders.size()).isEqualTo(5);
    }

    @Test
    void findAllByMerchant() throws Exception {
        //given

        //when
        List<Order> orders = orderRepository.findAllByMerchant(merchant1);

        //then
        assert orders != null;
        assertThat(orders.size()).isEqualTo(3);
    }

    @Test
    void findAllByCustomer() throws Exception {
        //given

        //when
        List<Order> orders = orderRepository.findAllByCustomer(customer1);

        //then
        assert orders != null;
        assertThat(orders.size()).isEqualTo(3);
    }

    @Test
    void findAllByStatus() throws Exception {
        //given

        //when
        List<Order> orders = orderRepository.findAllByStatus(OrderStatus.ORDER);

        //then
        assertThat(orders.size()).isEqualTo(3);
    }

    @Test
    void findAllByOrderDateBetween() throws Exception {
        //given
        LocalDateTime from = LocalDateTime.now().minusDays(3);
        LocalDateTime to = LocalDateTime.now().plusDays(1);

        //when
        List<Order> orders = orderRepository.findAllByOrderDateBetween(from, to);

        //then
        assertThat(orders.size()).isEqualTo(2);
    }

    @Test
    void findByOrderNumber() throws Exception {
        //given

        //when
        Order order = orderRepository.findByOrderNumber("12345");

        //then
        assertThat(order.getStatus()).isEqualTo(OrderStatus.DELIVERED);
    }

    @Test
    void findAllByItemName() throws Exception {
        //given

        //when
        List<Order> orders = orderRepository.findAllByItemName("item1");

        //then
        assertThat(orders.size()).isEqualTo(2);
    }

    @Test
    void findAllByMerchantName() throws Exception {
        //given

        //when
        List<Order> orders = orderRepository.findAllByMerchantName("merchant1");

        //then
        assertThat(orders.size()).isEqualTo(3);
    }

    @Test
    void findAllByCustomerPhoneNumber() throws Exception {
        //given

        //when
        List<Order> orders = orderRepository.findAllByCustomerPhoneNumber("01011111111");

        //then
        assertThat(orders.size()).isEqualTo(3);
    }

    @Test
    void findAllByCustomerLoginId() throws Exception {
        //given

        //when
        List<Order> orders = orderRepository.findAllByCustomerLoginId("customer2");

        //then
        assertThat(orders.size()).isEqualTo(2);
    }

    @Test
    void findAllByCustomerName() throws Exception {
        //given

        //when
        List<Order> orders = orderRepository.findAllByCustomerName("구매자일");

        //then
        assert orders != null;
        assertThat(orders.size()).isEqualTo(3);
    }

}