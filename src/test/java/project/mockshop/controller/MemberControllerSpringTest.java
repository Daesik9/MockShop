package project.mockshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import project.mockshop.dto.*;
import project.mockshop.entity.AddressInfo;
import project.mockshop.service.CouponService;
import project.mockshop.service.CustomerService;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class MemberControllerSpringTest {
    @Autowired
    private CustomerController customerController;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CouponService couponService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();
    Long userId;

    @BeforeEach
    void init() {
//        mockMvc = MockMvcBuilders
//                .standaloneSetup(customerController)
//                .apply(springSecurity())
//                .setControllerAdvice(new ExceptionAdvice()).build();

        CustomerCreationDto requestDto = CustomerCreationDto.builder()
                .loginId("loginid")
                .name("테스트")
                .password("Password1!")
                .phoneNumber("01011111111")
                .email("email@email.com")
                .addressInfo(new AddressInfo("city", "street", "88888"))
                .build();
        userId = customerService.createAccount(requestDto);
    }

    @Test
    void notNull() throws Exception {
        //given

        //when

        //then
        assertThat(customerController).isNotNull();
    }

    @Test
    void createAccount_success() throws Exception {
        //given
        CustomerCreationDto requestDto = CustomerCreationDto.builder()
                .loginId("newloginid")
                .name("테스트")
                .password("Password1!")
                .phoneNumber("01011111111")
                .email("email@email.com")
                .addressInfo(new AddressInfo("city", "street", "88888"))
                .build();


        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(201));
    }



    @Test
    void createAccount_fail() throws Exception {
        //given
        CustomerCreationDto customerRequest = CustomerCreationDto.builder()
                .loginId("loginid")
                .name("name")
                .password("Password1!")
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerRequest))
        );

        //then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400));
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    void findOne() throws Exception {
        //given

        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/members/{id}", userId)
        );

        //then
        resultActions.andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.result.data.loginId").value("loginid"));
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    void findAll() throws Exception {
        //given

        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/members")
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.data[0].name").value("테스트"));
    }

    @Test
        //아이디, 비번 찾기는 누구나 가능해야함. 로그인 한 유저만 가능한게 아니라
    void findLoginIdByEmail() throws Exception {
        //given
        String email = "email@email.com";
        FindLoginIdRequestDto requestDto = FindLoginIdRequestDto.builder().email("email@email.com").build();

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/api/members/find-login-id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.result.data").value("loginid"));
    }

    @Test
    void login() throws Exception {
        // Given
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .loginId("loginid")
                .password("Password1!")
                .build();

        // When
        ResultActions resultActions = mockMvc.perform(
                post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDto))
        );

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    void checkDuplicateLoginId() throws Exception {
        //given
        String duplicatedLoginId = "loginid";
        CustomerCreationDto requestDto = CustomerCreationDto.builder()
                .loginId(duplicatedLoginId)
                .name("테스트")
                .password("Password1!")
                .phoneNumber("01011111111")
                .email("email@email.com")
                .addressInfo(new AddressInfo("city", "street", "88888"))
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/members/check-duplicate/{loginId}", duplicatedLoginId)
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.result.data").value(true));
    }

    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    void updateProfile() throws Exception {
        //given
        UpdateProfileDto updateProfileDto = UpdateProfileDto.builder()
                .userId(userId)
                .name("새이름")
                .password("Newpassword1!")
                .email("newemail@gmail.com")
                .phoneNumber("01022222222")
                .addressInfo(new AddressInfo("new city", "new street", "99999"))
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.put("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateProfileDto))
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
        assertThat(customerService.findOne(userId).getName()).isEqualTo("새이름");
    }

    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    void getAllCouponItems() throws Exception {
        //given
        CouponDto couponDto = CouponDto.builder().build();
        CustomerCreationDto requestDto = CustomerCreationDto.builder()
                .loginId("newloginid")
                .name("테스트")
                .password("Password1!")
                .phoneNumber("01011111111")
                .email("email@email.com")
                .addressInfo(new AddressInfo("city", "street", "88888"))
                .build();
        Long customerId = customerService.createAccount(requestDto);
        Long couponId = couponService.createCoupon(couponDto);
        couponService.issueCoupon(couponId, customerId);

        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/members/{customerId}/coupons", customerId)
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.result.data[0].customerDto.id").value(customerId));
    }

    @Test
    void resetPassword() throws Exception {
        //given
        ResetPasswordDto resetPasswordDto = ResetPasswordDto.builder()
                .email("email@email.com")
                .password("NewPassword1!")
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(
                put("/api/members/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resetPasswordDto))
        );
        CustomerDto customerDto = customerService.findOne(userId);

        //then
        resultActions.andExpect(status().isOk());
        assertThat(passwordEncoder.matches("NewPassword1!", customerDto.getPassword())).isTrue();
    }

    @Test
    @WithMockUser(roles = {"CUSTOMER"})
    void getAvailableCoupons() throws Exception {
        //given
        CouponDto couponDtoPriceOff = CouponDto.builder()
                .name("coupon")
                .priceOff(4000)
                .expiredDate(LocalDateTime.now().plusDays(30))
                .minPriceRequired(5000)
                .build();
        CouponDto couponDtoPercentOff = CouponDto.builder()
                .name("coupon2")
                .percentOff(10)
                .expiredDate(LocalDateTime.now().plusDays(15))
                .minPriceRequired(1000)
                .maxPriceOff(2000)
                .build();
        CustomerCreationDto requestDto = CustomerCreationDto.builder()
                .loginId("newloginid")
                .name("테스트")
                .password("Password1!")
                .phoneNumber("01011111111")
                .email("email@email.com")
                .addressInfo(new AddressInfo("city", "street", "88888"))
                .build();
        Long customerId = customerService.createAccount(requestDto);
        Long couponId1 = couponService.createCoupon(couponDtoPriceOff);
        Long couponId2 = couponService.createCoupon(couponDtoPercentOff);
        couponService.issueCoupon(couponId1, customerId);
        couponService.issueCoupon(couponId2, customerId);

        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/members/{customerId}/coupons/available-for-order", customerId)
                        .param("orderAmount", "3000")
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.result.data[0].couponDto.name").value("coupon2"));
    }

}
