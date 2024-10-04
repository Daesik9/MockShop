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
import project.mockshop.dto.*;
import project.mockshop.entity.Cart;
import project.mockshop.entity.CartItem;
import project.mockshop.entity.Customer;
import project.mockshop.entity.Item;
import project.mockshop.mapper.CartMapper;
import project.mockshop.security.JwtUtil;
import project.mockshop.service.CartService;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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
    @Mock
    private JwtUtil jwtUtil;

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
    void changeCartItemCount() throws Exception {
        //given
        CartChangeRequestDto changeRequestDto = CartChangeRequestDto.builder()
                .cartId(1L)
                .cartItemId(1L)
                .count(4)
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.put("/api/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changeRequestDto))
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    void removeCartItem() throws Exception {
        //given
        CartDeleteRequestDto deleteRequestDto = CartDeleteRequestDto.builder()
                .cartId(1L)
                .cartItemId(1L)
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deleteRequestDto))
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
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
        Cart cart = Cart.builder()
                .cartItems(List.of(CartItem.builder().item(Item.builder().build()).count(3).cartPrice(3000).build()))
                .customer(Customer.builder().build())
                .build();
        given(cartService.getCartWithItems(1L)).willReturn(CartMapper.toDto(cart));

        //when
        when(jwtUtil.getLoginMemberId()).thenReturn(1L);
        ResultActions resultActions = mockMvc.perform(
                get("/api/cart")
//                        .param("customerId", "1")
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.data.cartItemDtos[0].count").value(3))
                .andExpect(jsonPath("$.result.data.cartItemDtos[0].cartPrice").value(3000));
    }

}