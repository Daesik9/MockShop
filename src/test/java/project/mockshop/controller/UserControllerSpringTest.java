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
import project.mockshop.entity.Address;
import project.mockshop.service.CustomerService;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
public class UserControllerSpringTest {
    @Autowired
    private CustomerController customerController;
    @Autowired
    private CustomerService customerService;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();
    Long userId;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(customerController)
                .setControllerAdvice(new ExceptionAdvice()).build();

        CustomerCreationDto requestDto = CustomerCreationDto.builder()
                .loginId("loginid")
                .name("테스트")
                .password("Password1!")
                .phoneNumber("01011111111")
                .email("email@email.com")
                .address(new Address("city", "street", "88888"))
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
                .address(new Address("city", "street", "88888"))
                .build();


        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/users")
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

        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/users/{id}", userId)
        );

        //then
        resultActions.andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.result.data.loginId").value("loginid"));
    }

    @Test
    void findAll() throws Exception {
        //given

        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/users")
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.data[0].name").value("테스트"));
    }

    @Test
    void findLoginId() throws Exception {
        //given
        String phoneNumber = "01011111111";
        FindLoginIdRequestDto requestDto = FindLoginIdRequestDto.builder().phoneNumber(phoneNumber).build();

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/api/users/find/login-id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
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
                post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDto))
        );

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.result.data.loginId").value("loginid"));
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
                .userId(userId)
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
        assertThat(customerService.findOne(userId).getName()).isEqualTo("새이름");
    }
}




