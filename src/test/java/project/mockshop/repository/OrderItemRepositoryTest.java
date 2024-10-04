package project.mockshop.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import project.mockshop.entity.Customer;
import project.mockshop.entity.Item;
import project.mockshop.entity.Order;
import project.mockshop.entity.OrderItem;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class OrderItemRepositoryTest {
    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Test
    void repositoryNotNull() throws Exception {
        //given

        //when

        //then
        assertThat(orderItemRepository).isNotNull();
    }

    @Test
    void create() throws Exception {
        //given
        OrderItem orderItem = OrderItem.builder()
                .item(Item.builder().name("item").quantity(100).build())
//                .order(Order.builder().build())
                .orderPrice(10000)
                .count(10)
                .build();

        //when
        OrderItem savedOrderItem = orderItemRepository.save(orderItem);


        //then
        assertThat(savedOrderItem.getId()).isEqualTo(orderItem.getId());
    }

    @Test
    void findById() throws Exception {
        //given
        OrderItem orderItem = OrderItem.builder()
                .item(Item.builder().name("item").quantity(100).build())
//                .order(Order.builder().build())
                .orderPrice(10000)
                .count(10)
                .build();
        orderItemRepository.save(orderItem);

        //when
        OrderItem foundOrderItem = orderItemRepository.findById(orderItem.getId()).orElse(null);

        //then
        assertThat(foundOrderItem.getItem().getName()).isEqualTo("item");
    }

    @Test
    void findAll() throws Exception {
        //given
        Item item1 = Item.builder().name("item1").quantity(100).build();
        Item item2 = Item.builder().name("item2").quantity(100).build();
        Item item3 = Item.builder().name("item3").quantity(100).build();
        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);

//        Customer customer = Customer.builder().build();
//        customerRepository.save(customer);

//        Order order = Order.builder()
////                .customer(customer)
//                .orderItems(List.of(OrderItem.builder().item(item1).build(),
//                        OrderItem.builder().item(item2).build(),
//                        OrderItem.builder().item(item3).build())
//                )
//                .build();
//        orderRepository.save(order);

        OrderItem orderItem1 = OrderItem.builder()
                .item(item1)
//                .order(order)
                .orderPrice(10000)
                .count(10)
                .build();

        OrderItem orderItem2 = OrderItem.builder()
                .item(item2)
//                .order(order)
                .orderPrice(10000)
                .count(10)
                .build();

        OrderItem orderItem3 = OrderItem.builder()
                .item(item3)
//                .order(order)
                .orderPrice(10000)
                .count(10)
                .build();

        orderItemRepository.save(orderItem1);
        orderItemRepository.save(orderItem2);
        orderItemRepository.save(orderItem3);

        //when
        List<OrderItem> orderItems = orderItemRepository.findAll();

        //then

        assertThat(orderItems.size()).isEqualTo(3);
    }

}