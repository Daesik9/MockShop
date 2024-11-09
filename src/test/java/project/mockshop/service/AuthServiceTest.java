package project.mockshop.service;

import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import project.mockshop.dto.AuthCodeDto;
import project.mockshop.dto.LoginRequestDto;
import project.mockshop.entity.*;
import project.mockshop.repository.AuthCodeRepository;
import project.mockshop.repository.MemberRepository;
import project.mockshop.security.JwtTokenProvider;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @InjectMocks
    private AuthService authService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @Mock
    private AuthCodeRepository authCodeRepository;
    @Mock
    private JavaMailSender javaMailSender;

    @Test
    void login_customer() throws Exception {
        //given
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .loginId("customer")
                .password("Customer1!")
                .build();

        Member member = Customer.builder()
                .id(1L)
                .loginId(loginRequestDto.getLoginId())
                .password(loginRequestDto.getPassword())
                .role(Role.CUSTOMER.name())
                .build();

        //when
        when(memberRepository.findByLoginId(loginRequestDto.getLoginId())).thenReturn(Optional.of(member));
        when(passwordEncoder.matches(loginRequestDto.getPassword(), member.getPassword())).thenReturn(true);
        String token = authService.login(loginRequestDto);

        //then
        when(jwtTokenProvider.getRole(token)).thenReturn(Role.CUSTOMER.name());
        assertThat(jwtTokenProvider.getRole(token)).isEqualTo(Role.CUSTOMER.name());
    }

    @Test
    void login_merchant() throws Exception {
        //given
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .loginId("merchant")
                .password("Merchant1!")
                .build();

        Member member = Merchant.builder()
                .id(1L)
                .loginId(loginRequestDto.getLoginId())
                .password(loginRequestDto.getPassword())
                .role(Role.MERCHANT.name())
                .build();

        //when
        when(memberRepository.findByLoginId(loginRequestDto.getLoginId())).thenReturn(Optional.of(member));
        when(passwordEncoder.matches(loginRequestDto.getPassword(), member.getPassword())).thenReturn(true);
        String token = authService.login(loginRequestDto);

        //then
        when(jwtTokenProvider.getRole(token)).thenReturn(Role.MERCHANT.name());
        assertThat(jwtTokenProvider.getRole(token)).isEqualTo(Role.MERCHANT.name());
    }

    @Test
    void login_admin() throws Exception {
        //given
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .loginId("admin")
                .password("Admin1!")
                .build();

        Member member = Admin.builder()
                .id(1L)
                .loginId(loginRequestDto.getLoginId())
                .password(loginRequestDto.getPassword())
                .role(Role.ADMIN.name())
                .build();

        //when
        when(memberRepository.findByLoginId(loginRequestDto.getLoginId())).thenReturn(Optional.of(member));
        when(passwordEncoder.matches(loginRequestDto.getPassword(), member.getPassword())).thenReturn(true);
        String token = authService.login(loginRequestDto);

        //then
        when(jwtTokenProvider.getRole(token)).thenReturn(Role.ADMIN.name());
        assertThat(jwtTokenProvider.getRole(token)).isEqualTo(Role.ADMIN.name());
    }

    @Test
    void verifyAuthCode() throws Exception {
        //given
        AuthCode authCode = AuthCode.builder()
                .code("123456")
                .email("email@email.com")
                .expirationTime(LocalDateTime.now().plusMinutes(3))
                .build();
        given(authCodeRepository.findByEmail("email@email.com")).willReturn(authCode);

        //when
        boolean isVerified = authService.verifyAuthCode(AuthCodeDto.builder().authCode("123456").email("email@email.com").build());

        //then
        assertThat(isVerified).isTrue();
    }

    @Test
    void sendAuthCode() throws Exception {
        //given
        Member member = Customer.builder()
                .id(1L)
                .loginId("loginid")
                .password("Password1!")
                .role(Role.CUSTOMER.name())
                .build();

        given(memberRepository.findByEmail("email@email.com")).willReturn(Optional.of(member));
        given(javaMailSender.createMimeMessage()).willReturn(new MimeMessage((Session) null));

        //when
        String code = authService.sendAuthCode("email@email.com");

        //then
        assertThat(code).isNotNull();
    }

    @Test
    void getIdFromToken() throws Exception {
        //given
        String token = "temp_token";
        given(jwtTokenProvider.getMemberId(token)).willReturn("1");
        given(memberRepository.findById(1L)).willReturn(Optional.of(Member.memberBuilder().id(1L).build()));

        //when
        String memberId = jwtTokenProvider.getMemberId(token);
        Member byId = memberRepository.findById(Long.valueOf(memberId))
                .orElseThrow();

        //then
        assertThat(byId.getId()).isEqualTo(1L);
    }
}