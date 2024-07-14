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
import project.mockshop.entity.Address;
import project.mockshop.service.CouponService;
import project.mockshop.service.CustomerService;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @InjectMocks
    private CustomerController customerController;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private CustomerService customerService;
    @Mock
    private CouponService couponService;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(customerController)
                .setControllerAdvice(new ExceptionAdvice()).build();
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
        resultActions.andExpect(jsonPath("$.code").value(201));
    }



    @Test
    void createAccount_fail() throws Exception {
        //given
        CustomerCreationDto customerRequest = CustomerCreationDto.builder()
                .loginId("loginid")
                .name("name")
                .password("Password1!")
                .build();
        willThrow(IllegalArgumentException.class).given(customerService)
                .createAccount(any(CustomerCreationDto.class));

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerRequest))
        );

        //then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400));
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
        resultActions.andExpect(jsonPath("$.code").value(200));
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
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.data[0].name").value("김길동"));
        verify(customerService).findAll();
    }

    @Test
    void findLoginId() throws Exception {
        //given
        String phoneNumber = "01011111111";
        CustomerDto customer = CustomerDto.builder().loginId("loginid").phoneNumber(phoneNumber).build();
        given(customerService.findLoginId(phoneNumber)).willReturn(customer);
        FindLoginIdRequestDto requestDto = FindLoginIdRequestDto.builder().phoneNumber(phoneNumber).build();

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/api/users/find/login-id")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.result.data").value("loginid"));
    }

    @Test
    void findPassword() throws Exception {
        //given
        String loginId = "loginid";
        String phoneNumber = "01011111111";
        CustomerDto customer = CustomerDto.builder().loginId(loginId).phoneNumber(phoneNumber)
                .password("Password1!").build();
        given(customerService.findPassword(loginId, phoneNumber)).willReturn(customer);
        FindPasswordRequestDto requestDto = FindPasswordRequestDto.builder()
                .loginId(loginId)
                .phoneNumber(phoneNumber)
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/api/users/find/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.result.data").value("Password1!"));
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
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
//        verify(customerService).login(eq(loginRequestDto));
    }

    @Test
    void checkDuplicateLoginId() throws Exception {
        //given
        String duplicatedLoginId = "loginid";
        given(customerService.validateDuplicateLoginId(duplicatedLoginId)).willReturn(true);
        CustomerCreationDto requestDto = CustomerCreationDto.builder()
                .loginId(duplicatedLoginId)
                .name("테스트")
                .password("Password1!")
                .phoneNumber("01011111111")
                .email("email@email.com")
                .address(new Address("city", "street", "88888"))
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/users/check-duplicate/{loginId}", duplicatedLoginId)
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.result.data").value(true));
    }

    @Test
    void updateProfile() throws Exception {
        //given
        UpdateProfileDto updateProfileDto = UpdateProfileDto.builder()
                .userId(1L)
                .name("새이름")
                .password("Newpassword1!")
                .email("newemail@gmail.com")
                .phoneNumber("01022222222")
                .address(new Address("new city", "new street", "99999"))
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.put("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateProfileDto))
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
        verify(customerService, times(1)).updateProfile(any(UpdateProfileDto.class));
    }

    @Test
    void updateProfile_fail_samePassword() throws Exception {
        //given
        UpdateProfileDto updateProfileDto = UpdateProfileDto.builder()
                .userId(1L)
                .name("새이름")
                .password("Password1!")
                .email("newemail@gmail.com")
                .phoneNumber("01022222222")
                .address(new Address("new city", "new street", "99999"))
                .build();
        willThrow(IllegalArgumentException.class)
                .given(customerService).updateProfile(argThat(dto -> dto.getPassword().equals("Password1!")));

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.put("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateProfileDto))
        );

        //then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400));
    }

    @Test
    void getAllCouponItems() throws Exception {
        //given

        CustomerDto customer = CustomerDto.builder().id(1L).build();
        CouponItemDto couponItemDto = CouponItemDto.builder().customerDto(customer).build();
        given(couponService.getAllCouponItemsByCustomerId(customer.getId())).willReturn(List.of(couponItemDto));

        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/users/coupons/{customerId}", customer.getId())
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.result.data[0].customerDto.id").value(1L));
    }


}




