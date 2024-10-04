package project.mockshop.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import project.mockshop.dto.LoginRequestDto;
import project.mockshop.entity.*;
import project.mockshop.repository.MemberRepository;
import project.mockshop.security.JwtTokenProvider;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @InjectMocks
    private AuthService authService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

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
        String token = authService.login(loginRequestDto);

        //then
        when(jwtTokenProvider.getRole(token)).thenReturn(Role.ADMIN.name());
        assertThat(jwtTokenProvider.getRole(token)).isEqualTo(Role.ADMIN.name());
    }


}