package project.mockshop.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import project.mockshop.dto.ItemCreationDto;
import project.mockshop.dto.ItemDto;
import project.mockshop.dto.ItemSearchCondition;
import project.mockshop.entity.*;
import project.mockshop.mapper.ItemMapper;
import project.mockshop.repository.CategoryRepository;
import project.mockshop.repository.ItemRepository;
import project.mockshop.repository.MerchantRepository;
import project.mockshop.repository.OrderRepository;

import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

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
        String path = "src/test/resources/image/image.png";

        FileInputStream fileInputStream = new FileInputStream(path);
        MultipartFile multipartFile = new MockMultipartFile(
                "img1",
                "img1.png",
                "png",
                fileInputStream);

        Merchant merchant = Merchant.builder().name("merchant").build();
        merchantRepository.save(merchant);

        ItemCreationDto itemCreationDto = ItemCreationDto.builder()
                .name("name")
                .category(category)
                .thumbnail(multipartFile)
                .price(1000)
                .quantity(100)
                .merchant(merchant)
                .percentOff(10)
                .build();

        //when
        Long itemId = itemService.createItem(itemCreationDto);


        //then
        Item findItem = itemRepository.findById(itemId).orElse(null);
        assertThat(findItem.getName()).isEqualTo("name");
        assertThat(findItem.getThumbnail().getUploadFileName()).isEqualTo("img1.png");
    }

    @Test
    void findItemsByName() throws Exception {
        //given
        Merchant merchant = Merchant.builder().name("merchant").build();
        ItemCreationDto itemDto1 = ItemCreationDto.builder()
                .name("asdfnameasdf")
                .category(category)
                .price(1000)
                .quantity(100)
                .percentOff(10)
                .build();
        ItemCreationDto itemDto2 = ItemCreationDto.builder()
                .name("asdfname")
                .category(category)
                .price(1000)
                .quantity(100)
                .percentOff(10)
                .build();
        ItemCreationDto itemDto3 = ItemCreationDto.builder()
                .name("nameasdf")
                .category(category)
                .price(1000)
                .quantity(100)
                .percentOff(10)
                .build();
        ItemCreationDto itemDto4 = ItemCreationDto.builder()
                .name("name")
                .category(category)
                .price(1000)
                .quantity(100)
                .percentOff(10)
                .build();
        ItemCreationDto itemDto5 = ItemCreationDto.builder()
                .name("asdfasdf")
                .category(category)
                .price(1000)
                .quantity(100)
                .percentOff(10)
                .build();

        itemService.createItem(itemDto1);
        itemService.createItem(itemDto2);
        itemService.createItem(itemDto3);
        itemService.createItem(itemDto4);
        itemService.createItem(itemDto5);

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

        ItemCreationDto itemDto1 = ItemCreationDto.builder()
                .name("asdfnameasdf")
                .category(category)
                .price(1000)
                .quantity(100)
                .percentOff(10)
                .merchant(merchant)
                .build();
        ItemCreationDto itemDto2 = ItemCreationDto.builder()
                .name("asdfname")
                .category(category)
                .price(1000)
                .quantity(100)
                .percentOff(10)
                .merchant(merchant)
                .build();
        ItemCreationDto itemDto3 = ItemCreationDto.builder()
                .name("nameasdf")
                .category(category)
                .price(1000)
                .quantity(100)
                .percentOff(10)
                .build();
        ItemCreationDto itemDto4 = ItemCreationDto.builder()
                .name("name")
                .category(category)
                .price(1000)
                .quantity(100)
                .percentOff(10)
                .build();
        ItemCreationDto itemDto5 = ItemCreationDto.builder()
                .name("asdfasdf")
                .category(category)
                .price(1000)
                .quantity(100)
                .percentOff(10)
                .build();

        itemService.createItem(itemDto1);
        itemService.createItem(itemDto2);
        itemService.createItem(itemDto3);
        itemService.createItem(itemDto4);
        itemService.createItem(itemDto5);

        //when
        List<ItemDto> findItems = itemService.findItemsByMerchantId(merchant.getId());

        //then
        assertThat(findItems.size()).isEqualTo(2);
        assertThat(findItems.get(0).getMerchant()).isEqualTo(merchant);
    }

    @Test
    void findItemsByCategory() throws Exception {
        //given
        Merchant merchant = Merchant.builder().name("merchant").build();
        Category category1 = new Category("category1");
        categoryRepository.save(category1);
        Long merchantId = 1L;
        ItemCreationDto itemDto1 = ItemCreationDto.builder()
                .name("asdfnameasdf")
                .category(category)
                .price(1000)
                .quantity(100)
                .percentOff(10)
                .build();
        ItemCreationDto itemDto2 = ItemCreationDto.builder()
                .name("asdfname")
                .category(category)
                .price(1000)
                .quantity(100)
                .percentOff(10)
                .build();
        ItemCreationDto itemDto3 = ItemCreationDto.builder()
                .name("nameasdf")
                .category(category)
                .price(1000)
                .quantity(100)
                .percentOff(10)
                .build();
        ItemCreationDto itemDto4 = ItemCreationDto.builder()
                .name("name")
                .category(category1)
                .price(1000)
                .quantity(100)
                .percentOff(10)
                .build();
        ItemCreationDto itemDto5 = ItemCreationDto.builder()
                .name("asdfasdf")
                .category(category1)
                .price(1000)
                .quantity(100)
                .percentOff(10)
                .build();

        itemService.createItem(itemDto1);
        itemService.createItem(itemDto2);
        itemService.createItem(itemDto3);
        itemService.createItem(itemDto4);
        itemService.createItem(itemDto5);

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

        ItemCreationDto itemDto1 = ItemCreationDto.builder()
                .name("asdfnameasdf")
                .category(category)
                .price(1000)
                .quantity(100)
                .percentOff(10)
                .build();
        ItemCreationDto itemDto2 = ItemCreationDto.builder()
                .name("asdfname")
                .category(category)
                .price(1000)
                .quantity(200)
                .percentOff(10)
                .build();
        ItemCreationDto itemDto3 = ItemCreationDto.builder()
                .name("nameasdf")
                .category(category)
                .price(1000)
                .quantity(300)
                .percentOff(10)
                .build();
        ItemCreationDto itemDto4 = ItemCreationDto.builder()
                .name("name")
                .category(category)
                .price(1000)
                .quantity(400)
                .percentOff(10)
                .build();
        ItemCreationDto itemDto5 = ItemCreationDto.builder()
                .name("asdfasdf")
                .category(category)
                .price(1000)
                .quantity(500)
                .percentOff(10)
                .build();

        itemService.createItem(itemDto1);
        itemService.createItem(itemDto2);
        itemService.createItem(itemDto3);
        itemService.createItem(itemDto4);
        itemService.createItem(itemDto5);

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
        ItemCreationDto itemDto1 = ItemCreationDto.builder()
                .name("asdfnameasdf")
                .category(category)
                .price(1000)
                .quantity(100)
                .percentOff(10)
                .build();
        ItemCreationDto itemDto2 = ItemCreationDto.builder()
                .name("asdfname")
                .category(category)
                .price(2000)
                .quantity(200)
                .percentOff(10)
                .build();
        ItemCreationDto itemDto3 = ItemCreationDto.builder()
                .name("nameasdf")
                .category(category)
                .price(3000)
                .quantity(300)
                .percentOff(10)
                .build();
        ItemCreationDto itemDto4 = ItemCreationDto.builder()
                .name("name")
                .category(category)
                .price(4000)
                .quantity(400)
                .percentOff(10)
                .build();
        ItemCreationDto itemDto5 = ItemCreationDto.builder()
                .name("asdfasdf")
                .category(category)
                .price(5000)
                .quantity(500)
                .percentOff(10)
                .build();

        itemService.createItem(itemDto1);
        itemService.createItem(itemDto2);
        itemService.createItem(itemDto3);
        itemService.createItem(itemDto4);
        itemService.createItem(itemDto5);

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
        ItemCreationDto itemDto1 = ItemCreationDto.builder()
                .name("asdfnameasdf")
                .category(category)
                .price(1000)
                .quantity(100)
                .percentOff(10)
                .build();
        ItemCreationDto itemDto2 = ItemCreationDto.builder()
                .name("asdfname")
                .category(category)
                .price(2000)
                .quantity(200)
                .build();
        ItemCreationDto itemDto3 = ItemCreationDto.builder()
                .name("nameasdf")
                .category(category)
                .price(3000)
                .quantity(300)
                .build();
        ItemCreationDto itemDto4 = ItemCreationDto.builder()
                .name("name")
                .category(category)
                .price(4000)
                .quantity(400)
                .percentOff(10)
                .build();
        ItemCreationDto itemDto5 = ItemCreationDto.builder()
                .name("asdfasdf")
                .category(category)
                .price(5000)
                .quantity(500)
                .percentOff(10)
                .build();


        itemService.createItem(itemDto1);
        itemService.createItem(itemDto2);
        itemService.createItem(itemDto3);
        itemService.createItem(itemDto4);
        itemService.createItem(itemDto5);

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
        List<ItemDto> items = itemService.findBestFiveThisWeek();

        //then
        assertThat(items.size()).isEqualTo(5);
        assertThat(items.get(0).getName()).isEqualTo("1등");
        assertThat(items.get(4).getName()).isEqualTo("5등");
        assertThat(items).doesNotContain(ItemMapper.toDto(item7));
    }

    @Test
    void search() throws Exception {
        //given
        Merchant merchant = Merchant.builder().name("merchant").build();
//        Category category = new Category("category");
        List<Item> items = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            Item item = Item.builder()
                    .name(i + "name" + i)
//                    .category(category)
//                    .thumbnail("thumbnail.png")
                    .price(1000 * (i + 1))
                    .quantity(100)
//                    .descriptionImg1("img1.png")
//                    .descriptionImg2("img2.png")
//                    .descriptionImg3("img3.png")
                    .percentOff((i + 1) % 2 == 0 ? 0 : 0.1) // 홀수번째 아이템만 할인.
                    .build();

            itemRepository.save(item);
            items.add(item);
        }

        OrderItem orderItem5 = OrderItem.builder().item(items.get(4)).count(10).build();
        OrderItem orderItem7 = OrderItem.builder().item(items.get(6)).count(5).build();

        Order order = Order.builder()
                .orderItems(List.of(orderItem5, orderItem7))
                .orderDate(LocalDateTime.now())
                .build();

        orderRepository.save(order);

        int page = 0;
        int size = 8;
        Pageable pageable = PageRequest.of(page, size);
        ItemSearchCondition searchCond = ItemSearchCondition.builder()
                .itemNameLike("name")
                .priceGoe(1000)
                .priceLoe(10000)
                .isOnSale(true)
                .sortBy("salesVolume")
                .build();

        //TODO: salesVolume으로 하는데 왜 63 얘가 제일 먼저 나오는거지?????
        //판매량순으로 필터했을 때 null인 경우 때문에 순서가 꼬임.
        //값이 같은 경우는 arbitrary하게 정렬이 됨.

        //when
        Page<ItemDto> findItems = itemService.search(searchCond, pageable);
        System.out.println("findItems = " + findItems.getContent());

        //then
        assertThat(findItems.getNumberOfElements()).isEqualTo(5);
        assertThat(findItems.getContent().get(0).getPrice()).isEqualTo(5000);
        assertThat(findItems.getContent().get(1).getPrice()).isEqualTo(7000);
    }
}