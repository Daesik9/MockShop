package project.mockshop.service;

import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import project.mockshop.dto.AuthCodeDto;
import project.mockshop.dto.LoginRequestDto;
import project.mockshop.entity.*;
import project.mockshop.repository.AuthCodeRepository;
import project.mockshop.repository.MemberRepository;
import project.mockshop.security.JwtTokenProvider;


import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@Transactional
public class AuthServiceSpringTest {
    @Autowired
    private AuthService authService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private AuthCodeRepository authCodeRepository;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private JavaMailSender javaMailSender;

    @Test
    void login_customer() throws Exception {
        //given
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .loginId("customer")
                .password("Customer1!")
                .build();

        Member member = Customer.builder()
                .loginId(loginRequestDto.getLoginId())
                .password(passwordEncoder.encode(loginRequestDto.getPassword()))
                .role(Role.CUSTOMER.name())
                .build();
        memberRepository.save(member);

        //when
        String token = authService.login(loginRequestDto);

        //then
        assertThat(jwtTokenProvider.getRole(token)).isEqualTo("ROLE_CUSTOMER");
        assertThat(jwtTokenProvider.getLoginId(token)).isEqualTo("customer");
    }

    @Test
    void login_merchant() throws Exception {
        //given
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .loginId("merchant")
                .password("Merchant1!")
                .build();

        Member member = Merchant.builder()
                .loginId(loginRequestDto.getLoginId())
                .password(passwordEncoder.encode(loginRequestDto.getPassword()))
                .role(Role.MERCHANT.name())
                .build();
        memberRepository.save(member);

        //when
        String token = authService.login(loginRequestDto);

        //then
        assertThat(jwtTokenProvider.getRole(token)).isEqualTo("ROLE_MERCHANT");
        assertThat(jwtTokenProvider.getLoginId(token)).isEqualTo("merchant");
    }

    @Test
    void login_admin() throws Exception {
        //given
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .loginId("admin")
                .password("Admin1!")
                .build();

        Member member = Admin.builder()
                .loginId(loginRequestDto.getLoginId())
                .password(passwordEncoder.encode(loginRequestDto.getPassword()))
                .role(Role.ADMIN.name())
                .build();
        memberRepository.save(member);
        System.out.println("member.getRole() = " + member.getRole());

        //when
        String token = authService.login(loginRequestDto);

        //then
        String role = jwtTokenProvider.getRole(token);
        assertThat(role).isEqualTo("ROLE_ADMIN");
        assertThat(jwtTokenProvider.getLoginId(token)).isEqualTo("admin");
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

        //when
        boolean isVerified = authService.verifyAuthCode(AuthCodeDto.builder()
                .authCode("123456")
                .email("email@email.com")
                .build());

        //then
        assertThat(isVerified).isTrue();
    }

    @Test
    void getIdFromToken() throws Exception {
        //given
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .loginId("customer")
                .password("Customer1!")
                .build();

        Member member = Customer.builder()
                .loginId(loginRequestDto.getLoginId())
                .password(passwordEncoder.encode(loginRequestDto.getPassword()))
                .role(Role.CUSTOMER.name())
                .build();
        memberRepository.save(member);
        String token = authService.login(loginRequestDto);

        //when
        String memberId = jwtTokenProvider.getMemberId(token);
        Member byId = memberRepository.findById(Long.valueOf(memberId))
                .orElseThrow();

        //then
        assertThat(byId.getId()).isEqualTo(member.getId());
    }

}