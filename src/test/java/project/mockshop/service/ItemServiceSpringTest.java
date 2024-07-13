package project.mockshop.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import project.mockshop.dto.ItemDto;
import project.mockshop.entity.*;
import project.mockshop.mapper.ItemMapper;
import project.mockshop.repository.CategoryRepository;
import project.mockshop.repository.ItemRepository;
import project.mockshop.repository.MerchantRepository;
import project.mockshop.repository.OrderRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@Transactional
public class ItemServiceSpringTest {
    @Autowired
    ItemService itemService;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    MerchantRepository merchantRepository;
    @Autowired
    OrderRepository orderRepository;

    Category category;
    @BeforeEach
    void beforeEach() {
        category = new Category("category");
        categoryRepository.save(category);
    }

    @Test
    void createItem() throws Exception {
        //given
        Merchant merchant = Merchant.builder().name("merchant").build();
        merchantRepository.save(merchant);

        ItemDto itemDto = ItemDto.builder()
                .name("name")
                .category(category)
                .thumbnail("thumbnail.png")
                .price(1000)
                .quantity(100)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .merchant(merchant)
                .percentOff(10)
                .build();

        //when
        Long itemId = itemService.createItem(itemDto, merchant.getId());


        //then
        Item findItem = itemRepository.findById(itemId).orElse(null);
        assertThat(findItem.getName()).isEqualTo("name");
    }

    @Test
    void findItemsByName() throws Exception {
        //given
        Merchant merchant = Merchant.builder().name("merchant").build();
        ItemDto itemDto1 = ItemDto.builder()
                .name("asdfnameasdf")
                .category(category)
                .thumbnail("thumbnail.png")
                .price(1000)
                .quantity(100)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .percentOff(10)
                .build();
        ItemDto itemDto2 = ItemDto.builder()
                .name("asdfname")
                .category(category)
                .thumbnail("thumbnail.png")
                .price(1000)
                .quantity(100)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .percentOff(10)
                .build();
        ItemDto itemDto3 = ItemDto.builder()
                .name("nameasdf")
                .category(category)
                .thumbnail("thumbnail.png")
                .price(1000)
                .quantity(100)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .percentOff(10)
                .build();
        ItemDto itemDto4 = ItemDto.builder()
                .name("name")
                .category(category)
                .thumbnail("thumbnail.png")
                .price(1000)
                .quantity(100)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .percentOff(10)
                .build();
        ItemDto itemDto5 = ItemDto.builder()
                .name("asdfasdf")
                .category(category)
                .thumbnail("thumbnail.png")
                .price(1000)
                .quantity(100)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .percentOff(10)
                .build();

        itemService.createItem(itemDto1, merchant.getId());
        itemService.createItem(itemDto2, merchant.getId());
        itemService.createItem(itemDto3, merchant.getId());
        itemService.createItem(itemDto4, merchant.getId());
        itemService.createItem(itemDto5, merchant.getId());

        //when
        List<ItemDto> findItems = itemService.findItemsByNameLike("name");

        for (ItemDto findItem : findItems) {
            System.out.println("findItem.getName() = " + findItem.getName());
        }

        //then
        assertThat(findItems.size()).isEqualTo(4);
    }

    @Test
    void findItemsByMerchantName() throws Exception {
        //given
        Merchant merchant = Merchant.builder().name("merchant").build();
        merchantRepository.save(merchant);

        ItemDto itemDto1 = ItemDto.builder()
                .name("asdfnameasdf")
                .category(category)
                .thumbnail("thumbnail.png")
                .price(1000)
                .quantity(100)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .percentOff(10)
                .merchant(merchant)
                .build();
        ItemDto itemDto2 = ItemDto.builder()
                .name("asdfname")
                .category(category)
                .thumbnail("thumbnail.png")
                .price(1000)
                .quantity(100)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .percentOff(10)
                .merchant(merchant)
                .build();
        ItemDto itemDto3 = ItemDto.builder()
                .name("nameasdf")
                .category(category)
                .thumbnail("thumbnail.png")
                .price(1000)
                .quantity(100)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .percentOff(10)
                .build();
        ItemDto itemDto4 = ItemDto.builder()
                .name("name")
                .category(category)
                .thumbnail("thumbnail.png")
                .price(1000)
                .quantity(100)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .percentOff(10)
                .build();
        ItemDto itemDto5 = ItemDto.builder()
                .name("asdfasdf")
                .category(category)
                .thumbnail("thumbnail.png")
                .price(1000)
                .quantity(100)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .percentOff(10)
                .build();

        itemService.createItem(itemDto1, merchant.getId());
        itemService.createItem(itemDto2, merchant.getId());
        itemService.createItem(itemDto3, merchant.getId());
        itemService.createItem(itemDto4, merchant.getId());
        itemService.createItem(itemDto5, merchant.getId());

        //when
        List<ItemDto> findItems = itemService.findItemsByMerchantName("merchant");

        //then
        assertThat(findItems.size()).isEqualTo(2);
    }

    @Test
    void findItemsByCategory() throws Exception {
        //given
        Merchant merchant = Merchant.builder().name("merchant").build();
        Category category1 = new Category("category1");
        categoryRepository.save(category1);
        Long merchantId = 1L;
        ItemDto itemDto1 = ItemDto.builder()
                .name("asdfnameasdf")
                .category(category)
                .thumbnail("thumbnail.png")
                .price(1000)
                .quantity(100)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .percentOff(10)
                .build();
        ItemDto itemDto2 = ItemDto.builder()
                .name("asdfname")
                .category(category)
                .thumbnail("thumbnail.png")
                .price(1000)
                .quantity(100)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .percentOff(10)
                .build();
        ItemDto itemDto3 = ItemDto.builder()
                .name("nameasdf")
                .category(category)
                .thumbnail("thumbnail.png")
                .price(1000)
                .quantity(100)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .percentOff(10)
                .build();
        ItemDto itemDto4 = ItemDto.builder()
                .name("name")
                .category(category1)
                .thumbnail("thumbnail.png")
                .price(1000)
                .quantity(100)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .percentOff(10)
                .build();
        ItemDto itemDto5 = ItemDto.builder()
                .name("asdfasdf")
                .category(category1)
                .thumbnail("thumbnail.png")
                .price(1000)
                .quantity(100)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .percentOff(10)
                .build();

        itemService.createItem(itemDto1, merchantId);
        itemService.createItem(itemDto2, merchantId);
        itemService.createItem(itemDto3, merchantId);
        itemService.createItem(itemDto4, merchantId);
        itemService.createItem(itemDto5, merchantId);

        //when
        List<ItemDto> findItems = itemService.findAllByCategory(category);

        //then
        assertThat(findItems.size()).isEqualTo(3);
    }

    @Test
    void findItemsByQuantity() throws Exception {
        //given
        Merchant merchant = Merchant.builder().name("merchant").build();
        merchantRepository.save(merchant);

        ItemDto itemDto1 = ItemDto.builder()
                .name("asdfnameasdf")
                .category(category)
                .thumbnail("thumbnail.png")
                .price(1000)
                .quantity(100)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .percentOff(10)
                .merchant(merchant)
                .build();
        ItemDto itemDto2 = ItemDto.builder()
                .name("asdfname")
                .category(category)
                .thumbnail("thumbnail.png")
                .price(1000)
                .quantity(200)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .percentOff(10)
                .merchant(merchant)
                .build();
        ItemDto itemDto3 = ItemDto.builder()
                .name("nameasdf")
                .category(category)
                .thumbnail("thumbnail.png")
                .price(1000)
                .quantity(300)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .percentOff(10)
                .merchant(merchant)
                .build();
        ItemDto itemDto4 = ItemDto.builder()
                .name("name")
                .category(category)
                .thumbnail("thumbnail.png")
                .price(1000)
                .quantity(400)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .percentOff(10)
                .merchant(merchant)
                .build();
        ItemDto itemDto5 = ItemDto.builder()
                .name("asdfasdf")
                .category(category)
                .thumbnail("thumbnail.png")
                .price(1000)
                .quantity(500)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .percentOff(10)
                .merchant(merchant)
                .build();

        itemService.createItem(itemDto1, merchant.getId());
        itemService.createItem(itemDto2, merchant.getId());
        itemService.createItem(itemDto3, merchant.getId());
        itemService.createItem(itemDto4, merchant.getId());
        itemService.createItem(itemDto5, merchant.getId());

        //when
        List<ItemDto> findItems = itemService.findItemsByQuantity(0, 300);

        //then
        assertThat(findItems.size()).isEqualTo(3);
        assertThatThrownBy(() -> itemService.findItemsByQuantity(-100, 300))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> itemService.findItemsByQuantity(100, -300))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> itemService.findItemsByQuantity(-100, -300))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> itemService.findItemsByQuantity(300, 100))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void findItemsByPrice() throws Exception {
        //given
        Merchant merchant = Merchant.builder().name("merchant").build();
        ItemDto itemDto1 = ItemDto.builder()
                .name("asdfnameasdf")
                .category(category)
                .thumbnail("thumbnail.png")
                .price(1000)
                .quantity(100)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .percentOff(10)
                .build();
        ItemDto itemDto2 = ItemDto.builder()
                .name("asdfname")
                .category(category)
                .thumbnail("thumbnail.png")
                .price(2000)
                .quantity(200)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .percentOff(10)
                .build();
        ItemDto itemDto3 = ItemDto.builder()
                .name("nameasdf")
                .category(category)
                .thumbnail("thumbnail.png")
                .price(3000)
                .quantity(300)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .percentOff(10)
                .build();
        ItemDto itemDto4 = ItemDto.builder()
                .name("name")
                .category(category)
                .thumbnail("thumbnail.png")
                .price(4000)
                .quantity(400)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .percentOff(10)
                .build();
        ItemDto itemDto5 = ItemDto.builder()
                .name("asdfasdf")
                .category(category)
                .thumbnail("thumbnail.png")
                .price(5000)
                .quantity(500)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .percentOff(10)
                .build();

        itemService.createItem(itemDto1, merchant.getId());
        itemService.createItem(itemDto2, merchant.getId());
        itemService.createItem(itemDto3, merchant.getId());
        itemService.createItem(itemDto4, merchant.getId());
        itemService.createItem(itemDto5, merchant.getId());

        //when
        List<ItemDto> findItems = itemService.findItemsByPrice(2000, 4000);

        //then
        assertThat(findItems.size()).isEqualTo(3);
        assertThatThrownBy(() -> itemService.findItemsByPrice(-2000, 4000))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> itemService.findItemsByPrice(2000, -4000))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> itemService.findItemsByPrice(-2000, -4000))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> itemService.findItemsByPrice(4000, 2000))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void findDiscountItems() throws Exception {
        //given
        Merchant merchant = Merchant.builder().name("merchant").build();
        ItemDto itemDto1 = ItemDto.builder()
                .name("asdfnameasdf")
                .category(category)
                .thumbnail("thumbnail.png")
                .price(1000)
                .quantity(100)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .percentOff(10)
                .build();
        ItemDto itemDto2 = ItemDto.builder()
                .name("asdfname")
                .category(category)
                .thumbnail("thumbnail.png")
                .price(2000)
                .quantity(200)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .build();
        ItemDto itemDto3 = ItemDto.builder()
                .name("nameasdf")
                .category(category)
                .thumbnail("thumbnail.png")
                .price(3000)
                .quantity(300)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .build();
        ItemDto itemDto4 = ItemDto.builder()
                .name("name")
                .category(category)
                .thumbnail("thumbnail.png")
                .price(4000)
                .quantity(400)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .percentOff(10)
                .build();
        ItemDto itemDto5 = ItemDto.builder()
                .name("asdfasdf")
                .category(category)
                .thumbnail("thumbnail.png")
                .price(5000)
                .quantity(500)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .percentOff(10)
                .build();

        itemService.createItem(itemDto1, merchant.getId());
        itemService.createItem(itemDto2, merchant.getId());
        itemService.createItem(itemDto3, merchant.getId());
        itemService.createItem(itemDto4, merchant.getId());
        itemService.createItem(itemDto5, merchant.getId());

        //when
        List<ItemDto> findItems = itemService.findDiscountItems();

        //then
        assertThat(findItems.size()).isEqualTo(3);
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
                .orderDate(LocalDateTime.now().minusDays(4))
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
        List<ItemDto> items = itemService.findBestFiveThisWeek();

        //then
        assertThat(items.size()).isEqualTo(5);
        assertThat(items.get(0).getName()).isEqualTo("1등");
        assertThat(items.get(4).getName()).isEqualTo("5등");
        assertThat(items).doesNotContain(ItemMapper.toDto(item7));
    }
}