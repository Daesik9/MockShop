package project.mockshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import project.mockshop.advice.ExceptionAdvice;
import project.mockshop.dto.ItemCreationDto;
import project.mockshop.dto.ItemSearchCondition;
import project.mockshop.entity.*;
import project.mockshop.repository.ItemRepository;
import project.mockshop.repository.OrderRepository;
import project.mockshop.service.OrderService;

import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
public class ItemControllerSpringTest {

    @Autowired
    private ItemController itemController;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private OrderRepository orderRepository;
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(itemController)
                .setControllerAdvice(new ExceptionAdvice())
                .build();
    }

    @Test
    void create() throws Exception {
        //given
        String path = "src/test/resources/image/image.png";
        FileInputStream fileInputStream = new FileInputStream(path);
        MockMultipartFile thumb = new MockMultipartFile(
                "thumbnail",
                "thumb.png",
                "png",
                fileInputStream
        );

        FileInputStream fileInputStream2 = new FileInputStream(path);
        MockMultipartFile img1 = new MockMultipartFile(
                "descriptionImg1",
                "img1.png",
                "png",
                fileInputStream2
        );

        ItemCreationDto creationDto = ItemCreationDto.builder()
                .name("name")
                .category(new Category("category"))
                .thumbnail(thumb)
                .price(1000)
                .quantity(100)
                .descriptionImg1(img1)
                .percentOff(10)
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(
                multipart("/api/items")
                        .file(thumb).file(img1)
                        .param("name", creationDto.getName())
                        .param("price", String.valueOf(creationDto.getPrice()))
                        .param("quantity", String.valueOf(creationDto.getQuantity()))
                        .param("percentOff", String.valueOf(creationDto.getPercentOff()))
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        List<Item> items = itemRepository.findAll();
        Item item = items.get(0);
        assertThat(item.getName()).isEqualTo("name");
        assertThat(item.getThumbnail().getUploadFileName()).isEqualTo("thumb.png");
        assertThat(item.getDescriptionImg1().getUploadFileName()).isEqualTo("img1.png");
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
        ResultActions resultActions = mockMvc.perform(
                get("/api/items/best-five")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.result.data[0].name").value("1등"))
                .andExpect(jsonPath("$.result.data[4].name").value("5등"));
    }

    @Test
    void search() throws Exception {
        //given
        Merchant merchant = Merchant.builder().name("merchant").build();
        Category category = new Category("category");
        List<Item> items = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            Item item = Item.builder()
                    .name(i + "name" + i)
//                    .category(category)
                    .thumbnail(UploadFile.builder().storeFileName("thumbnail.png").build())
                    .price(1000 * (i + 1))
                    .quantity(100)
                    .descriptionImg1(UploadFile.builder().storeFileName("img1.png").build())
                    .descriptionImg2(UploadFile.builder().storeFileName("img2.png").build())
                    .descriptionImg3(UploadFile.builder().storeFileName("img3.png").build())
                    .percentOff((i + 1) % 2 == 0 ? 0 : 0.1) // 홀수번째 아이템만 할인.
                    .build();

            itemRepository.save(item);
            items.add(item);
        }

        int page = 0;
        int size = 8;
        Pageable pageable = PageRequest.of(page, size);
        ItemSearchCondition searchCond = ItemSearchCondition.builder()
                .itemNameLike("name")
                .priceGoe(3000)
                .priceLoe(10000)
                .isOnSale(true)
                .sortBy("salesVolume")
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/items/search")
                        .param("itemNameLike", searchCond.getItemNameLike())
                        .param("priceGoe", searchCond.getPriceGoe().toString())
                        .param("sortBy", searchCond.getSortBy())
                        .param("page", String.valueOf(pageable.getPageNumber()))
                        .param("size", String.valueOf(pageable.getPageSize()))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.result.data.content[0].name").value("2name2"))
                .andExpect(jsonPath("$.result.data.content[0].price").value(3000));
    }

}