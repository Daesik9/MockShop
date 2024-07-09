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
import project.mockshop.dto.CartAddRequestDto;
import project.mockshop.dto.ItemDto;
import project.mockshop.entity.Customer;
import project.mockshop.mapper.CustomerMapper;
import project.mockshop.service.CartService;
import project.mockshop.service.CustomerService;
import project.mockshop.service.ItemService;

import static org.junit.jupiter.api.Assertions.*;
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
    void getCartItems() throws Exception {
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
                .andExpect(jsonPath("$.result.data[0].count").value(3))
                .andExpect(jsonPath("$.result.data[0].cartPrice").value(3000));
    }


}