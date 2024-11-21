package project.mockshop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import project.mockshop.dto.ItemCreationDto;
import project.mockshop.dto.ItemDto;
import project.mockshop.dto.ItemSearchCondition;
import project.mockshop.dto.ItemThumbDto;
import project.mockshop.entity.*;
import project.mockshop.mapper.ItemMapper;
import project.mockshop.repository.ItemRepository;
import project.mockshop.repository.MerchantRepository;
import project.mockshop.repository.OrderRepository;
import project.mockshop.util.FileStore;

import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

/**
 * 관리자는 상품명, 판매자명, 카테고리, 상품재고, 가격, 할인여부로 검색이 가능하다.
 * 구매자
 * 검색은 상품명으로만 검색이 가능하다. 검색 단어가 포함된 모든 상품을 검색한다.
 * 낮은가격순, 높은가격순, 판매량순, 최신순으로 정렬을 제공한다.
 * 금액대, 할인여부로 상품들 필터링 할 수 있는 기능을 제공한다.
 */
@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {
    @InjectMocks
    private ItemService itemService;

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private MerchantRepository merchantRepository;
    @Mock
    private FileStore fileStore;

    Category category;
    Merchant merchant;

    @BeforeEach
    void beforeEach() {
        category = Category.builder().name("category").build();
        merchant = Merchant.builder().name("merchant").storeName("merchant_store").build();
    }

    @Test
    void itemServiceAndRepositoryNotNull() throws Exception {
        //given

        //when

        //then
        assertThat(itemService).isNotNull();
        assertThat(itemRepository).isNotNull();
    }

    @Test
    void createItem() throws Exception {
        //given
        String path = "src/test/resources/image/image.png";
        FileInputStream fileInputStream = new FileInputStream(path);
        MockMultipartFile image1 = new MockMultipartFile(
                "img1",
                "img1.png",
                "png",
                fileInputStream
        );

        ItemCreationDto itemCreationDto = ItemCreationDto.builder()
                .name("name")
                .category(category)
                .thumbnail(image1)
                .price(1000)
                .quantity(100)
                .descriptionImg1(image1)
                .descriptionImg2(image1)
                .descriptionImg3(image1)
                .percentOff(10)
                .build();

        //when
        Long itemId = itemService.createItem(itemCreationDto);
        Item item = ItemMapper.toEntity(itemCreationDto);
        when(fileStore.createUploadFile(itemCreationDto.getThumbnail())).thenReturn(UploadFile.builder().uploadFileName("img1.png").build());
        item.changeThumbnail(fileStore.createUploadFile(itemCreationDto.getThumbnail()));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));


        //then
        Item findItem = itemRepository.findById(itemId).orElse(null);
        assertThat(findItem.getName()).isEqualTo("name");
        assertThat(findItem.getThumbnail().getUploadFileName()).isEqualTo("img1.png");
    }

    @Test
    void findItemsByName() throws Exception {
        //given
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
                .merchant(merchant)
                .build();
        ItemCreationDto itemDto4 = ItemCreationDto.builder()
                .name("name")
                .category(category)
                .price(1000)
                .quantity(100)
                .percentOff(10)
                .merchant(merchant)
                .build();
        ItemCreationDto itemDto5 = ItemCreationDto.builder()
                .name("asdfasdf")
                .category(category)
                .price(1000)
                .quantity(100)
                .percentOff(10)
                .merchant(merchant)
                .build();

        itemService.createItem(itemDto1);
        itemService.createItem(itemDto2);
        itemService.createItem(itemDto3);
        itemService.createItem(itemDto4);
        itemService.createItem(itemDto5);
        Stream<ItemCreationDto> itemDtos = Stream.of(itemDto1, itemDto2, itemDto3, itemDto4);

        given(itemRepository.findAllByNameLike("%name%"))
                .willReturn(itemDtos.map(ItemMapper::toEntity).toList());

        //when
        List<ItemDto> findItems = itemService.findItemsByNameLike("name");

        //then
        assertThat(findItems.size()).isEqualTo(4);
        verify(itemRepository, times(1)).findAllByNameLike("%name%");
    }

    @Test
    void search() throws Exception {
        //given
        List<ItemThumbDto> itemThumbDtos = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            ItemCreationDto itemCreationDto = ItemCreationDto.builder()
                    .name(i + "name" + i)
                    .category(category)
                    .price(1000)
                    .quantity(100)
                    .percentOff(10)
                    .merchant(merchant)
                    .build();

            ItemThumbDto itemThumbDto = ItemThumbDto.builder()
                    .name(i + "name" + i)
                    .price(1000)
                    .percentOff(10)
                    .build();

            itemService.createItem(itemCreationDto);
            itemThumbDtos.add(itemThumbDto);
        }

        Page<ItemThumbDto> pages = new PageImpl<>(itemThumbDtos.subList(0, 8));

        int page = 0;
        int size = 8;
        Pageable pageable = PageRequest.of(page, size);
        ItemSearchCondition searchCond = ItemSearchCondition.builder()
                .itemNameLike("name")
                .build();

        given(itemRepository.search(searchCond, pageable)).willReturn(pages);

        //when
        Page<ItemThumbDto> findItems = itemService.search(searchCond, pageable);

        //then
        assertThat(findItems.getSize()).isEqualTo(8);
        verify(itemRepository, times(1)).search(searchCond, pageable);
    }

    @Test
    void findItemsByMerchant() throws Exception {
        //given
        Merchant merchant1 = Merchant.builder().id(1L).name("merchant1").storeName("merchant_store1").build();

        ItemCreationDto itemDto1 = ItemCreationDto.builder()
                .name("asdfnameasdf")
                .category(category)
                .price(1000)
                .quantity(100)
                .percentOff(10)
                .merchant(merchant1)
                .build();
        ItemCreationDto itemDto2 = ItemCreationDto.builder()
                .name("asdfname")
                .category(category)
                .price(1000)
                .quantity(100)
                .percentOff(10)
                .merchant(merchant1)
                .build();
        ItemCreationDto itemDto3 = ItemCreationDto.builder()
                .name("nameasdf")
                .category(category)
                .price(1000)
                .quantity(100)
                .percentOff(10)
                .merchant(merchant)
                .build();
        ItemCreationDto itemDto4 = ItemCreationDto.builder()
                .name("name")
                .category(category)
                .price(1000)
                .quantity(100)
                .percentOff(10)
                .merchant(merchant)
                .build();
        ItemCreationDto itemDto5 = ItemCreationDto.builder()
                .name("asdfasdf")
                .category(category)
                .price(1000)
                .quantity(100)
                .percentOff(10)
                .merchant(merchant)
                .build();

        itemService.createItem(itemDto1);
        itemService.createItem(itemDto2);
        itemService.createItem(itemDto3);
        itemService.createItem(itemDto4);
        itemService.createItem(itemDto5);
        Stream<ItemCreationDto> itemDtos = Stream.of(itemDto1, itemDto2);

        given(merchantRepository.findById(merchant.getId())).willReturn(Optional.ofNullable(merchant));
        given(itemRepository.findAllByMerchant(merchant))
                .willReturn(itemDtos.map(ItemMapper::toEntity).toList());

        //when
        List<ItemDto> findItems = itemService.findItemsByMerchantId(merchant.getId());

        //then
        assertThat(findItems.size()).isEqualTo(2);
        assertThat(findItems.get(0).getMerchantDto().getName()).isEqualTo("merchant1");
        assertThat(findItems.get(0).getMerchantDto().getStoreName()).isEqualTo("merchant_store1");
    }

    @Test
    void findItemsByCategory() throws Exception {
        //given
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
                .merchant(merchant)
                .build();
        ItemCreationDto itemDto4 = ItemCreationDto.builder()
                .name("name")
                .price(1000)
                .quantity(100)
                .percentOff(10)
                .merchant(merchant)
                .build();
        ItemCreationDto itemDto5 = ItemCreationDto.builder()
                .name("asdfasdf")
                .price(1000)
                .quantity(100)
                .percentOff(10)
                .merchant(merchant)
                .build();

        itemService.createItem(itemDto1);
        itemService.createItem(itemDto2);
        itemService.createItem(itemDto3);
        itemService.createItem(itemDto4);
        itemService.createItem(itemDto5);
        Stream<ItemCreationDto> itemDtos = Stream.of(itemDto1, itemDto2, itemDto3);

        given(itemRepository.findAllByCategory(category))
                .willReturn(itemDtos.map(ItemMapper::toEntity).toList());

        //when
        List<ItemDto> findItems = itemService.findAllByCategory(category);

        //then
        assertThat(findItems.size()).isEqualTo(3);
        verify(itemRepository, times(1)).findAllByCategory(category);
    }

    @Test
    void findItemsByQuantity_0to300() throws Exception {
        //given
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
                .quantity(200)
                .percentOff(10)
                .merchant(merchant)
                .build();
        ItemCreationDto itemDto3 = ItemCreationDto.builder()
                .name("nameasdf")
                .category(category)
                .price(1000)
                .quantity(300)
                .percentOff(10)
                .merchant(merchant)
                .build();
        ItemCreationDto itemDto4 = ItemCreationDto.builder()
                .name("name")
                .category(category)
                .price(1000)
                .quantity(400)
                .percentOff(10)
                .merchant(merchant)
                .build();
        ItemCreationDto itemDto5 = ItemCreationDto.builder()
                .name("asdfasdf")
                .category(category)
                .price(1000)
                .quantity(500)
                .percentOff(10)
                .merchant(merchant)
                .build();

        itemService.createItem(itemDto1);
        itemService.createItem(itemDto2);
        itemService.createItem(itemDto3);
        itemService.createItem(itemDto4);
        itemService.createItem(itemDto5);
        Stream<ItemCreationDto> itemDtos = Stream.of(itemDto1, itemDto2, itemDto3);
        given(itemRepository.findAllByQuantity(0, 300))
                .willReturn(itemDtos.map(ItemMapper::toEntity).toList());

        //when
        List<ItemDto> findItems = itemService.findItemsByQuantity(0, 300);

        //then
        assertThat(findItems.size()).isEqualTo(3);
        verify(itemRepository, times(1)).findAllByQuantity(0, 300);

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
                .price(2000)
                .quantity(200)
                .percentOff(10)
                .merchant(merchant)
                .build();
        ItemCreationDto itemDto3 = ItemCreationDto.builder()
                .name("nameasdf")
                .category(category)
                .price(3000)
                .quantity(300)
                .percentOff(10)
                .merchant(merchant)
                .build();
        ItemCreationDto itemDto4 = ItemCreationDto.builder()
                .name("name")
                .category(category)
                .price(4000)
                .quantity(400)
                .percentOff(10)
                .merchant(merchant)
                .build();
        ItemCreationDto itemDto5 = ItemCreationDto.builder()
                .name("asdfasdf")
                .category(category)
                .price(5000)
                .quantity(500)
                .percentOff(10)
                .merchant(merchant)
                .build();

        itemService.createItem(itemDto1);
        itemService.createItem(itemDto2);
        itemService.createItem(itemDto3);
        itemService.createItem(itemDto4);
        itemService.createItem(itemDto5);
        Stream<ItemCreationDto> itemDtos = Stream.of(itemDto2, itemDto3, itemDto4);
        given(itemRepository.findAllByPrice(2000, 4000))
                .willReturn(itemDtos.map(ItemMapper::toEntity).toList());

        //when
        List<ItemDto> findItems = itemService.findItemsByPrice(2000, 4000);

        //then
        assertThat(findItems.size()).isEqualTo(3);
        verify(itemRepository, times(1)).findAllByPrice(2000, 4000);
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
                .price(2000)
                .quantity(200)
                .merchant(merchant)
                .build();
        ItemCreationDto itemDto3 = ItemCreationDto.builder()
                .name("nameasdf")
                .category(category)
                .price(3000)
                .quantity(300)
                .merchant(merchant)
                .build();
        ItemCreationDto itemDto4 = ItemCreationDto.builder()
                .name("name")
                .category(category)
                .price(4000)
                .quantity(400)
                .percentOff(10)
                .merchant(merchant)
                .build();
        ItemCreationDto itemDto5 = ItemCreationDto.builder()
                .name("asdfasdf")
                .category(category)
                .price(5000)
                .quantity(500)
                .percentOff(10)
                .merchant(merchant)
                .build();

        itemService.createItem(itemDto1);
        itemService.createItem(itemDto2);
        itemService.createItem(itemDto3);
        itemService.createItem(itemDto4);
        itemService.createItem(itemDto5);
        Stream<ItemCreationDto> itemDtos = Stream.of(itemDto1, itemDto4, itemDto5);
        given(itemRepository.findAllByDiscount())
                .willReturn(itemDtos.map(ItemMapper::toEntity).toList());

        //when
        List<ItemDto> findItems = itemService.findDiscountItems();

        //then
        assertThat(findItems.size()).isEqualTo(3);
        verify(itemRepository, times(1)).findAllByDiscount();
    }

    @Test
    void getBestFiveItemsThisWeek() throws Exception {
        //given
        Item item1 = Item.builder().name("1등").quantity(100).price(1000).merchant(merchant).category(category).build();
        Item item2 = Item.builder().name("2등").quantity(100).price(1000).merchant(merchant).category(category).build();
        Item item3 = Item.builder().name("3등").quantity(100).price(1000).merchant(merchant).category(category).build();
        Item item4 = Item.builder().name("4등").quantity(100).price(1000).merchant(merchant).category(category).build();
        Item item5 = Item.builder().name("5등").quantity(100).price(1000).merchant(merchant).category(category).build();
        Item item6 = Item.builder().name("6등").quantity(100).price(1000).merchant(merchant).category(category).build();
        Item item7 = Item.builder().name("7등").quantity(100).price(1000).merchant(merchant).category(category).build();
        Item item8 = Item.builder().name("8등").quantity(100).price(1000).merchant(merchant).category(category).build();
        Item item9 = Item.builder().name("9등").quantity(100).price(1000).merchant(merchant).category(category).build();
        Item item10 = Item.builder().name("10등").quantity(100).price(1000).merchant(merchant).category(category).build();
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
        given(itemRepository.findBestFiveThisWeek()).willReturn(List.of(item1, item2, item3, item4, item5));

        //when
        List<ItemDto> items = itemService.findBestFiveThisWeek();

        //then
        assertThat(items.size()).isEqualTo(5);
        assertThat(items.get(0).getName()).isEqualTo("1등");
        assertThat(items.get(4).getName()).isEqualTo("5등");
        assertThat(items).doesNotContain(ItemMapper.toDto(item7));
    }
}