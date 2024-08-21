package project.mockshop.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import project.mockshop.entity.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class ItemRepositoryTest {
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    OrderRepository orderRepository;

    @Test
    void repositoryNotNull() throws Exception {
        //given

        //when
        assertThat(itemRepository).isNotNull();

        //then
    }

    @Test
    void create() throws Exception {
        //given
        Item item = Item.builder().name("item").price(1000).quantity(100).build();

        //when
        Item savedItem = itemRepository.save(item);

        //then
        assertThat(savedItem.getId()).isEqualTo(item.getId());
    }

    @Test
    void findAll() throws Exception {
        //given
        Item item1 = Item.builder().name("item1").price(1000).quantity(100).build();
        Item item2 = Item.builder().name("item2").price(2000).quantity(200).build();
        Item item3 = Item.builder().name("item3").price(3000).quantity(300).build();
        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);

        //when
        List<Item> items = itemRepository.findAll();

        //then
        assertThat(items.size()).isEqualTo(3);
        assertThat(items.get(1)).isEqualTo(item2);
    }

    @Test
    void findById() throws Exception {
        //given
        Item item = Item.builder().name("item").price(1000).quantity(100).build();
        itemRepository.save(item);

        //when
        Optional<Item> findItem = itemRepository.findById(item.getId());

        //then
        assertThat(findItem).isNotEmpty();
        assertThat(findItem.get().getName()).isEqualTo("item");
    }

    @Test
    void delete() throws Exception {
        //given
        Item item1 = Item.builder().name("item1").price(1000).quantity(100).build();
        Item item2 = Item.builder().name("item2").price(2000).quantity(200).build();
        Item item3 = Item.builder().name("item3").price(3000).quantity(300).build();
        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);

        //when
        itemRepository.delete(item2);

        //then
        List<Item> items = itemRepository.findAll();
        assertThat(items.size()).isEqualTo(2);
        assertThat(items.get(1)).isEqualTo(item3);
    }

    @Test
    void update() throws Exception {
        //given
        Item item1 = Item.builder().name("item1").price(1000).quantity(100).build();
        Item item2 = Item.builder().name("item2").price(2000).quantity(200).build();
        Item item3 = Item.builder().name("item3").price(3000).quantity(300).build();
        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);
        Optional<Item> findItemOptional = itemRepository.findById(item2.getId());
        Item findItem = findItemOptional.orElse(null);

        //when
        assert findItem != null;
        findItem.changeName("newName");
        Category category = new Category("newCategory");
        findItem.changeCategory(category);
        findItem.changePrice(10000);
        findItem.changeQuantity(10000);
        findItem.changeThumbnail(UploadFile.builder().storeFileName("new-thumbnail.png").build());
        findItem.changeDescriptionImg1(UploadFile.builder().storeFileName("new-image1.png").build());
        findItem.changeDescriptionImg2(UploadFile.builder().storeFileName("new-image2.png").build());
        findItem.changeDescriptionImg3(UploadFile.builder().storeFileName("new-image3.png").build());
        findItem.changePercentOff(0.7);

        //then
        Optional<Item> changedItemOptional = itemRepository.findById(findItem.getId());
        assertThat(changedItemOptional).isNotEmpty();
        Item changedItem = changedItemOptional.get();
        assertThat(changedItem.getName()).isEqualTo("newName");
        assertThat(changedItem.getCategory()).isEqualTo(category);
        assertThat(changedItem.getPrice()).isEqualTo(10000);
        assertThat(changedItem.getQuantity()).isEqualTo(10000);
        assertThat(changedItem.getThumbnail().getStoreFileName()).isEqualTo("new-thumbnail.png");
        assertThat(changedItem.getDescriptionImg1().getStoreFileName()).isEqualTo("new-image1.png");
        assertThat(changedItem.getDescriptionImg2().getStoreFileName()).isEqualTo("new-image2.png");
        assertThat(changedItem.getDescriptionImg3().getStoreFileName()).isEqualTo("new-image3.png");
        assertThat(changedItem.getPercentOff()).isEqualTo(0.7);
    }

    @Test
    void getBestFiveItemsThisWeek() throws Exception {
        //given
        Item item1 = Item.builder().name("1등").quantity(100).price(1000).build();
        Item item2 = Item.builder().name("2등").quantity(100).price(1000).build();
        Item item3 = Item.builder().name("3등").quantity(100).price(1000).build();
        Item item4 = Item.builder().name("4등").quantity(100).price(1000).build();
        Item item5 = Item.builder().name("5등").quantity(100).price(1000).build();
        Item item6 = Item.builder().name("6등").quantity(100).price(1000).build();
        Item item7 = Item.builder().name("7등").quantity(100).price(1000).build();
        Item item8 = Item.builder().name("8등").quantity(100).price(1000).build();
        Item item9 = Item.builder().name("9등").quantity(100).price(1000).build();
        Item item10 = Item.builder().name("10등").quantity(100).price(1000).build();
        itemRepository.saveAll(List.of(item1, item2, item3, item4, item5, item6, item7, item8, item9, item10));

        OrderItem orderItem1 = OrderItem.builder().item(item1).count(1).build();
        OrderItem orderItem2 = OrderItem.builder().item(item2).count(4).build();
        OrderItem orderItem3 = OrderItem.builder().item(item3).count(3).build();
        OrderItem orderItem4 = OrderItem.builder().item(item4).count(2).build();
        OrderItem orderItem5 = OrderItem.builder().item(item5).count(1).build();
        Order order = Order.builder()
                .orderItems(List.of(orderItem1, orderItem2, orderItem3, orderItem4, orderItem5))
                .orderDate(LocalDateTime.now())
                .build();

        OrderItem orderItem6 = OrderItem.builder().item(item1).count(10).build();
        Order order2 = Order.builder()
                .orderItems(List.of(orderItem6))
                .orderDate(LocalDateTime.now().minusDays(1))
                .build();

        OrderItem orderItem7 = OrderItem.builder().item(item7).count(50).build();
        Order order3 = Order.builder()
                .orderItems(List.of(orderItem7))
                .orderDate(LocalDateTime.now().minusDays(10))
                .build();

        orderRepository.save(order);
        orderRepository.save(order2);
        orderRepository.save(order3);

        //when
        List<Item> items = itemRepository.findBestFiveThisWeek();

        //then
        assertThat(items.size()).isEqualTo(5);
        assertThat(items.get(0).getName()).isEqualTo("1등");
        assertThat(items.get(4).getName()).isEqualTo("5등");
        assertThat(items).doesNotContain(item7);
    }
    
    
}