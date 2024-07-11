package project.mockshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import project.mockshop.advice.ExceptionAdvice;
import project.mockshop.dto.CustomerDto;
import project.mockshop.dto.OrderDto;
import project.mockshop.dto.OrderRequestDto;
import project.mockshop.service.OrderService;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderService orderService;

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
        assertThat(mockMvc).isNotNull();
        assertThat(orderController).isNotNull();
        assertThat(orderService).isNotNull();
    }

    @Test
    void order() throws Exception {
        //given
        OrderRequestDto orderRequestDto = OrderRequestDto.builder()
                .customerId(1L)
                .paymentMethod("MOCK_PAY")
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequestDto))
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
        Mockito.verify(orderService).order(1L, "MOCK_PAY");
    }

    @Test
    void getOrderHistory() throws Exception {
        //given
        Long customerId = 1L;
        OrderDto orderDto = OrderDto.builder().customerDto(CustomerDto.builder().id(customerId).build()).build();
        given(orderService.findAllByCustomerId(customerId)).willReturn(List.of(orderDto));


        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/orders/customer/{customerId}", customerId)
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.result.data[0].customerDto.id").value(1L));
    }

    @Test
    void getOrderDetail() throws Exception {
        //given
        String orderNumber = "000001";
        given(orderService.findByOrderNumber(orderNumber)).willReturn(OrderDto.builder().orderNumber(orderNumber).build());

        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/orders/{orderNumber}", orderNumber)
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.result.data.orderNumber").value(orderNumber));
    }



}