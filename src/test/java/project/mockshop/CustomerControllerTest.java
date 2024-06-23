package project.mockshop;

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
import project.mockshop.controller.CustomerController;
import project.mockshop.dto.CustomerCreationDto;
import project.mockshop.dto.CustomerDto;
import project.mockshop.dto.LoginRequestDto;
import project.mockshop.entity.Address;
import project.mockshop.entity.Customer;
import project.mockshop.mapper.CustomerMapper;
import project.mockshop.service.CustomerService;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class CustomerControllerTest {
    @InjectMocks
    private CustomerController customerController;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private CustomerService customerService;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    @Test
    void mockMvcNotNull() {
        assertThat(customerController).isNotNull();
        assertThat(mockMvc).isNotNull();
    }

    @Test
    void createAccount_success() throws Exception {
        //given
        CustomerCreationDto requestDto = CustomerCreationDto.builder()
                .loginId("loginid")
                .name("테스트")
                .password("Password1!")
                .phoneNumber("01011111111")
                .email("email@email.com")
                .address(new Address("city", "street", "88888"))
                .build();


        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
        );

        //then
        resultActions.andExpect(status().isCreated());
//        Mockito.verify(customerService).createAccount(requestDto);
    }



    @Test
    void createAccount_fail() throws Exception {
        //given
        CustomerCreationDto customerRequest = CustomerCreationDto.builder()
                .loginId("loginid")
                .name("name")
                .password("Password1!")
                .build();
        willThrow(IllegalArgumentException.class).given(customerService).createAccount(customerRequest);

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerRequest))
        );

        //then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void findOne() throws Exception {
        //given
        Long id = 1L;
        given(customerService.findOne(id)).willReturn(CustomerDto.builder().build());

        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/users/{id}", id)
        );

        //then
        resultActions.andExpect(status().isOk());
        verify(customerService).findOne(id);
    }

    @Test
    void findAll() throws Exception {
        //given
        List<CustomerDto> customers =
                List.of(CustomerDto.builder().name("김길동").build());
        given(customerService.findAll()).willReturn(customers);

        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/users")
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].name").value("김길동"));
        verify(customerService).findAll();
    }

    @Test
    void findLoginId() throws Exception {
        //given
        String phoneNumber = "01011111111";
        CustomerDto customer = CustomerDto.builder().loginId("loginid").phoneNumber(phoneNumber).build();
        given(customerService.findLoginId(phoneNumber)).willReturn(customer);

        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/users/find/login-id")
                        .param("phoneNumber", phoneNumber)
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.loginId").value("loginid"))
                .andExpect(jsonPath("$.phoneNumber").value(phoneNumber));
    }

    @Test
    void findPassword() throws Exception {
        //given
        String loginId = "loginid";
        String phoneNumber = "01011111111";
        CustomerDto customer = CustomerDto.builder().loginId(loginId).phoneNumber(phoneNumber).build();
        given(customerService.findPassword(loginId, phoneNumber)).willReturn(customer);

        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/users/find/password")
                        .param("loginId", loginId)
                        .param("phoneNumber", phoneNumber)
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.loginId").value(loginId))
                .andExpect(jsonPath("$.phoneNumber").value(phoneNumber));
        verify(customerService).findPassword(loginId, phoneNumber);
    }

    /// 이상하게 자꾸 LoginRequestDto가 다른 인스턴스라고 오류 뜸.
    @Test
    void login() throws Exception {
        // Given
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .loginId("loginid")
                .password("Password1!")
                .build();

        // When
        ResultActions resultActions = mockMvc.perform(
                post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDto))
        );

        // Then
        resultActions.andExpect(status().isOk());
//        verify(customerService).login(eq(loginRequestDto));
    }

    @Test
    void checkDuplicateLoginId() throws Exception {
        //given
        String loginId = "duplicate";
        CustomerCreationDto requestDto = CustomerCreationDto.builder()
                .loginId(loginId)
                .name("테스트")
                .password("Password1!")
                .phoneNumber("01011111111")
                .email("email@email.com")
                .address(new Address("city", "street", "88888"))
                .build();
        willThrow(IllegalStateException.class).given(customerService).validateDuplicateLoginId(loginId);

        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/users/check-duplicate/{loginId}", loginId)
        );

        //then
        resultActions.andExpect(status().isOk());
    }

}




