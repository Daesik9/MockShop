package project.mockshop.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

public class AuthCodeTest {

    @Test
    void authCode() throws Exception {
        //given

        //when
        AuthCode authCode = AuthCode.builder()
                .code("123456")
                .email("email@email.com")
                .expirationTime(LocalDateTime.now().plusMinutes(3))
                .build();

        //then
        assertThat(authCode.getCode()).isEqualTo("123456");
        assertThat(authCode.getEmail()).isEqualTo("email@email.com");
        assertThat(authCode.getExpirationTime()).isNotNull();
    }

    @Test
    void authCode_isExpired() throws Exception {
        //given

        //when
        AuthCode authCode = AuthCode.builder()
                .code("123456")
                .email("email@email.com")
                .expirationTime(LocalDateTime.now().minusMinutes(3))
                .build();

        //then
        assertThat(authCode.isExpired()).isTrue();
    }




}