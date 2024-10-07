package project.mockshop.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import project.mockshop.dto.LoginRequestDto;
import project.mockshop.entity.*;
import project.mockshop.repository.MemberRepository;
import project.mockshop.security.JwtTokenProvider;


import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class AuthServiceSpringTest {
    @Autowired
    private AuthService authService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

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
        assertThat(jwtTokenProvider.getRole(token)).isEqualTo(Role.CUSTOMER.name());
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
        assertThat(jwtTokenProvider.getRole(token)).isEqualTo(Role.MERCHANT.name());
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
        assertThat(role).isEqualTo(Role.ADMIN.name());
        assertThat(jwtTokenProvider.getLoginId(token)).isEqualTo("admin");
    }
}