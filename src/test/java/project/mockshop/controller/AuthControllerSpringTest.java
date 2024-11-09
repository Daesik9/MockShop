package project.mockshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import project.mockshop.dto.AuthCodeDto;
import project.mockshop.dto.EmailDto;
import project.mockshop.dto.LoginRequestDto;
import project.mockshop.entity.AuthCode;
import project.mockshop.entity.Customer;
import project.mockshop.entity.Member;
import project.mockshop.entity.Role;
import project.mockshop.repository.AuthCodeRepository;
import project.mockshop.repository.MemberRepository;
import project.mockshop.response.Response;
import project.mockshop.service.AuthService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class AuthControllerSpringTest {
    @Autowired
    AuthController authController;
    @Autowired
    AuthService authService;
    @Autowired
    AuthCodeRepository authCodeRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void notNull() throws Exception {
        //given

        //when

        //then
        assertThat(authController).isNotNull();
        assertThat(mockMvc).isNotNull();
    }

    @Test
    void verifyAuthCode() throws Exception {
        //given
        AuthCode authCode = AuthCode.builder()
                .code("123456")
                .email("email@email.com")
                .expirationTime(LocalDateTime.now().plusMinutes(3))
                .build();
        authCodeRepository.save(authCode);

        AuthCodeDto authCodeDto = AuthCodeDto.builder()
                .authCode("123456")
                .email("email@email.com")
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/auth/verify-auth-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authCodeDto))
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.result.data").value(true));
    }

    @Test
    void getMemberIdByToken() throws Exception {
        //given
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .loginId("test")
                .password("Password1!")
                .build();

        Member member = Customer.builder()
                .loginId(loginRequestDto.getLoginId())
                .password(passwordEncoder.encode(loginRequestDto.getPassword()))
                .role(Role.CUSTOMER.name())
                .build();
        memberRepository.save(member);

        String token = authService.login(loginRequestDto);


        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/auth/member-id")
                        .header("Authorization", "Bearer " + token)
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.result.data").value(member.getId()));
    }

}