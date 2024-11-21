package project.mockshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import project.mockshop.advice.ExceptionAdvice;
import project.mockshop.dto.ItemCreationDto;
import project.mockshop.dto.ItemDto;
import project.mockshop.dto.ItemSearchCondition;
import project.mockshop.dto.ItemThumbDto;
import project.mockshop.entity.*;
import project.mockshop.mapper.CategoryMapper;
import project.mockshop.mapper.ItemMapper;
import project.mockshop.mapper.MerchantMapper;
import project.mockshop.service.ItemService;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ItemControllerTest {
    @InjectMocks
    private ItemController itemController;

    @Mock
    private ItemService itemService;

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
        MockMultipartFile image1 = new MockMultipartFile(
                "img1",
                "img1.png",
                "png",
                fileInputStream
        );

        ItemCreationDto creationDto = ItemCreationDto.builder()
                .name("name")
                .category(new Category("category"))
                .thumbnail(image1)
                .price(1000)
                .quantity(100)
                .descriptionImg1(image1)
                .descriptionImg2(image1)
                .descriptionImg3(image1)
                .percentOff(10)
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(
                multipart("/api/items")
                        .file(image1)
                        .param("name", creationDto.getName())
                        .param("price", String.valueOf(creationDto.getPrice()))
                        .param("quantity", String.valueOf(creationDto.getQuantity()))
                        .param("percentOff", String.valueOf(creationDto.getPercentOff()))
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }


    @Test
    void getBestFiveItemsThisWeek() throws Exception {
        //given
        Category category = new Category("category");
        Merchant merchant = Merchant.builder().name("merchant").storeName("merchant_store").build();

        Item item1 = Item.builder().name("1등").quantity(100).price(1000).merchant(merchant).category(category).build();
        Item item2 = Item.builder().name("2등").quantity(100).price(1000).merchant(merchant).category(category).build();
        Item item3 = Item.builder().name("3등").quantity(100).price(1000).merchant(merchant).category(category).build();
        Item item4 = Item.builder().name("4등").quantity(100).price(1000).merchant(merchant).category(category).build();
        Item item5 = Item.builder().name("5등").quantity(100).price(1000).merchant(merchant).category(category).build();
        given(itemService.findBestFiveThisWeek())
                .willReturn(Stream.of(item1, item2, item3, item4, item5).map(ItemMapper::toDto).toList());

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
        verify(itemService).findBestFiveThisWeek();
    }

    @Test
    void search() throws Exception {
        //given
        Merchant merchant = Merchant.builder().name("merchant").build();
        Category category = new Category("category");
        List<ItemThumbDto> itemThumbDtos = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            ItemCreationDto itemCreationDto = ItemCreationDto.builder()
                    .name(i + "name" + i)
                    .category(category)
//                    .thumbnail("thumbnail.png")
                    .price(1000 * (i + 1))
                    .quantity(100)
//                    .descriptionImg1("img1.png")
//                    .descriptionImg2("img2.png")
//                    .descriptionImg3("img3.png")
                    .percentOff((i + 1) % 2 == 0 ? 0 : 0.1) // 홀수번째 아이템만 할인.
                    .build();

            ItemThumbDto itemThumbDto = ItemThumbDto.builder()
                    .name(i + "name" + i)
                    .price(1000 * (i + 1))
                    .percentOff((i + 1) % 2 == 0 ? 0 : 0.1) // 홀수번째 아이템만 할인.
                    .build();

            itemService.createItem(itemCreationDto);
            itemThumbDtos.add(itemThumbDto);
        }


        int page = 0;
        int size = 8;
        Pageable pageable = PageRequest.of(page, size);
        Slice<ItemThumbDto> pages = new SliceImpl<>(itemThumbDtos.subList(0, 8), pageable, true);
        ItemSearchCondition searchCond = ItemSearchCondition.builder()
                .itemNameLike("name")
                .priceGoe(1000)
                .priceLoe(10000)
                .isOnSale(true)
                .sortBy("salesVolume")
                .build();

        given(itemService.searchSlice(any(ItemSearchCondition.class), any(Pageable.class))).willReturn(pages);

        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/items/search")
                        .param("itemNameLike", searchCond.getItemNameLike())
                        .param("priceGoe", searchCond.getPriceGoe().toString())
                        .param("priceLoe", searchCond.getPriceLoe().toString())
                        .param("isOnSale", searchCond.getIsOnSale().toString())
                        .param("sortBy", searchCond.getSortBy())
                        .param("page", "0")
                        .param("size", "8")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.result.data.content[0].name").value("0name0"));
        verify(itemService, times(1)).searchSlice(any(ItemSearchCondition.class), any(Pageable.class));
    }

    @Test
    void getItemsByMerchant() throws Exception {
        //given
        Merchant merchant = Merchant.builder().id(1L).name("merchant").build();
        List<ItemDto> itemDtos = List.of(ItemDto.builder().merchantDto(MerchantMapper.toDto(merchant)).build());

        given(itemService.findItemsByMerchantId(merchant.getId())).willReturn(itemDtos);

        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/merchants/{merchantId}/items", merchant.getId())
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.result.data[0].merchantDto.name").value("merchant"));
    }




}