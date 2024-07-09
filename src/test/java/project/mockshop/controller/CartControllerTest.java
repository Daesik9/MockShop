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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import project.mockshop.advice.ExceptionAdvice;
import project.mockshop.dto.CartAddRequestDto;
import project.mockshop.dto.CartItemDto;
import project.mockshop.dto.CustomerCreationDto;
import project.mockshop.dto.CustomerDto;
import project.mockshop.entity.Address;
import project.mockshop.service.CartService;

import java.util.DoubleSummaryStatistics;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class CartControllerTest {
    @InjectMocks
    private CartController cartController;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private CartService cartService;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(cartController)
                .setControllerAdvice(new ExceptionAdvice()).build();
    }

    @Test
    void mockMvcNotNull() {
        assertThat(cartController).isNotNull();
        assertThat(mockMvc).isNotNull();
    }

    @Test
    void addToCart() throws Exception {
        //given
        CartAddRequestDto cartRequest = CartAddRequestDto.builder().itemId(1L).count(3).build();

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartRequest))
        );

        //then
        resultActions.andExpect(jsonPath("$.code").value(200));
    }

    @Test
    void getCartItems() throws Exception {
        //given
        List<CartItemDto> cartItemDtos = List.of(CartItemDto.builder().count(3).cartPrice(3000).build());
        given(cartService.getCartItems(1L)).willReturn(cartItemDtos);

        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/cart")
                        .param("customerId", "1")
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.data[0].count").value(3))
                .andExpect(jsonPath("$.result.data[0].cartPrice").value(3000));
    }

}