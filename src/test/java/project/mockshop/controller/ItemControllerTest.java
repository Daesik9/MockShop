package project.mockshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import project.mockshop.advice.ExceptionAdvice;
import project.mockshop.dto.ItemDto;
import project.mockshop.dto.ItemSearchCondition;
import project.mockshop.entity.*;
import project.mockshop.mapper.ItemMapper;
import project.mockshop.service.ItemService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
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
    void getBestFiveItemsThisWeek() throws Exception {
        //given
        Item item1 = Item.builder().name("1등").quantity(100).price(1000).build();
        Item item2 = Item.builder().name("2등").quantity(100).price(1000).build();
        Item item3 = Item.builder().name("3등").quantity(100).price(1000).build();
        Item item4 = Item.builder().name("4등").quantity(100).price(1000).build();
        Item item5 = Item.builder().name("5등").quantity(100).price(1000).build();
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
        List<ItemDto> itemDtos = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            ItemDto itemDto = ItemDto.builder()
                    .name(i + "name" + i)
                    .category(category)
                    .thumbnail("thumbnail.png")
                    .price(1000 * (i + 1))
                    .quantity(100)
                    .descriptionImg1("img1.png")
                    .descriptionImg2("img2.png")
                    .descriptionImg3("img3.png")
                    .percentOff((i + 1) % 2 == 0 ? 0 : 0.1) // 홀수번째 아이템만 할인.
                    .build();

            itemService.createItem(itemDto, merchant.getId());
            itemDtos.add(itemDto);
        }


        int page = 0;
        int size = 8;
        Pageable pageable = PageRequest.of(page, size);
        Page<ItemDto> pages = new PageImpl<>(itemDtos.subList(0, 8), pageable, itemDtos.size());
        ItemSearchCondition searchCond = ItemSearchCondition.builder()
                .itemNameLike("name")
                .priceGoe(1000)
                .priceLoe(10000)
                .isOnSale(true)
                .sortBy("salesVolume")
                .build();

        given(itemService.search(any(ItemSearchCondition.class), any(Pageable.class))).willReturn(pages);

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
        verify(itemService, times(1)).search(any(ItemSearchCondition.class), any(Pageable.class));
    }


}