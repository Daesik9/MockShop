package project.mockshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import project.mockshop.advice.ExceptionAdvice;
import project.mockshop.dto.ItemDto;
import project.mockshop.entity.Item;
import project.mockshop.entity.Order;
import project.mockshop.entity.OrderItem;
import project.mockshop.mapper.ItemMapper;
import project.mockshop.service.ItemService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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
}