package project.mockshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import project.mockshop.dto.OrderRequestDto;
import project.mockshop.service.OrderService;

import static org.assertj.core.api.Assertions.*;

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
                MockMvcRequestBuilders.post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequestDto))
        );

        //then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200));
        Mockito.verify(orderService).order(1L, "MOCK_PAY");
    }


}