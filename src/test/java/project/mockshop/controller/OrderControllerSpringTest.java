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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import project.mockshop.advice.ExceptionAdvice;
import project.mockshop.dto.*;
import project.mockshop.entity.*;
import project.mockshop.service.CartService;
import project.mockshop.service.CustomerService;
import project.mockshop.service.ItemService;
import project.mockshop.service.OrderService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class OrderControllerSpringTest {

    @Autowired
    private OrderController orderController;

    @Autowired
    private OrderService orderService;
    @Autowired
    private CartService cartService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private CustomerService customerService;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController)
                .setControllerAdvice(new ExceptionAdvice())
                .build();
    }

    @Test
    void notNull() throws Exception {
        //given

        //when

        //then
        assertThat(orderController).isNotNull();
    }

    @Test
    void order() throws Exception {
        //given
        ItemDto itemDto = ItemDto.builder().name("사과").quantity(100).price(1000).build();
        Long itemId = itemService.createItem(itemDto, 1L);
        CustomerCreationDto userCreationDto = CustomerCreationDto.builder()
                .name("테스트")
                .loginId("loginid")
                .password("Password1!")
                .phoneNumber("01011111111")
                .email("email@gmail.com")
                .address(new Address("city", "street", "11111"))
                .build();
        Long customerId = customerService.createAccount(userCreationDto);
        Long cartId = cartService.addToCart(itemId, 10, customerId);

        OrderRequestDto orderRequestDto = OrderRequestDto.builder()
                .customerId(customerId)
                .paymentMethod("MOCK_PAY")
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequestDto))
        );

        //then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200));


        List<OrderDto> orderDtos = orderService.findAll();
        assertThat(orderDtos).isNotEmpty();
        assertThat(orderDtos.get(0).getCustomer().getId()).isEqualTo(customerId);
        assertThat(orderDtos.get(0).getPaymentMethod()).isEqualTo("MOCK_PAY");

        // 장바구니가 비워졌는지 확인
        CartDto cartWithItems = cartService.getCartWithItems(customerId);
        assertThat(cartWithItems.getCartItemDtos()).isEmpty();
    }

}