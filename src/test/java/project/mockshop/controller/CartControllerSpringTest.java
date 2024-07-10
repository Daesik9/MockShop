package project.mockshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import project.mockshop.advice.ExceptionAdvice;
import project.mockshop.dto.*;
import project.mockshop.entity.Customer;
import project.mockshop.mapper.CustomerMapper;
import project.mockshop.service.CartService;
import project.mockshop.service.CustomerService;
import project.mockshop.service.ItemService;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@Transactional
public class CartControllerSpringTest {
    @Autowired
    private CartController cartController;
    @Autowired
    private CartService cartService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private CustomerService customerService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    Long itemId;
    Long customerId;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(cartController)
                .setControllerAdvice(new ExceptionAdvice()).build();

        itemId = itemService.createItem(ItemDto.builder().name("사과").price(1000).quantity(100).build(), 1L);

        Customer customer = Customer.builder().name("테스트").password("Password1!").email("test@gmail.com").loginId("test").phoneNumber("01011111111").build();
        customerId = customerService.createAccount(CustomerMapper.toCreationDto(customer));
    }

    @Test
    void mockMvcNotNull() {
        assertThat(cartController).isNotNull();
        assertThat(mockMvc).isNotNull();
    }

    @Test
    void addToCart() throws Exception {
        //given
        CartAddRequestDto cartRequest = CartAddRequestDto.builder().itemId(itemId).count(3).build();

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
    void getCartWithItems() throws Exception {
        //given
        cartService.addToCart(itemId, 3, customerId);

        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/cart")
                        .param("customerId", customerId.toString())
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.data.cartItemDtos[0].count").value(3))
                .andExpect(jsonPath("$.result.data.cartItemDtos[0].cartPrice").value(3000));
    }

    @Test
    void changeCartItemCount() throws Exception {
        //given
        Long cartId = cartService.addToCart(itemId, 3, customerId);
        CartDto cartDtoWithItems = cartService.getCartWithItems(customerId);
        List<CartItemDto> cartItems = cartDtoWithItems.getCartItemDtos();

        CartChangeRequestDto changeRequestDto = CartChangeRequestDto.builder()
                .cartId(cartId)
                .cartItemId(cartItems.get(0).getId())
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
        assertThat(cartService.getCartWithItems(customerId).getCartItemDtos().get(0).getCount()).isEqualTo(4);
        assertThat(cartService.getCartWithItems(customerId).getCartItemDtos().get(0).getCartPrice()).isEqualTo(4000);
    }

    @Test
    void changeCartItemCount_fail() throws Exception {
        //given
        Long cartId = cartService.addToCart(itemId, 3, customerId);
        CartDto cartWithItems = cartService.getCartWithItems(customerId);
        List<CartItemDto> cartItems = cartWithItems.getCartItemDtos();

        CartChangeRequestDto changeRequestDto = CartChangeRequestDto.builder()
                .cartId(cartId)
                .cartItemId(cartItems.get(0).getId())
                .count(101)
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.put("/api/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changeRequestDto))
        );

        //then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.result.msg").value("재고가 부족합니다."));
        assertThat(cartService.getCartWithItems(customerId).getCartItemDtos().get(0).getCount()).isEqualTo(3);
        assertThat(cartService.getCartWithItems(customerId).getCartItemDtos().get(0).getCartPrice()).isEqualTo(3000);
    }

    @Test
    void removeCartItem() throws Exception {
        //given
        Long cartId = cartService.addToCart(itemId, 3, customerId);
        CartDto cartWithItems = cartService.getCartWithItems(customerId);
        List<CartItemDto> cartItems = cartWithItems.getCartItemDtos();
        CartDeleteRequestDto deleteRequestDto = CartDeleteRequestDto.builder()
                .cartId(cartId)
                .cartItemId(cartItems.get(0).getId())
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
        assertThat(cartService.getCartWithItems(customerId).getCartItemDtos().size()).isEqualTo(0);
    }

    @Test
    void removeCartItem_fail() throws Exception {
        //given
        Long cartId = cartService.addToCart(itemId, 3, customerId);
        CartDeleteRequestDto deleteRequestDto = CartDeleteRequestDto.builder()
                .cartId(cartId)
                .cartItemId(1L)
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deleteRequestDto))
        );

        //then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.result.msg").value("장바구니에 해당 상품이 없습니다."));
    }


}