package project.mockshop.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import project.mockshop.entity.AuthCode;
import project.mockshop.entity.Cart;
import project.mockshop.entity.CartItem;
import project.mockshop.entity.Item;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class AuthCodeRepositoryTest {
    @Autowired
    AuthCodeRepository authCodeRepository;

    @Test
    void notNull() throws Exception {
        //given

        //when

        //then
        assertThat(authCodeRepository).isNotNull();
    }

    @Test
    void findByEmail() throws Exception {
        //given
        String email = "test@test.com";
        AuthCode authCode = AuthCode.builder()
                .email(email)
                .code("123456")
                .expirationTime(LocalDateTime.now().plusMinutes(3))
                .build();
        authCodeRepository.save(authCode);

        //when
        AuthCode byEmail = authCodeRepository.findByEmail(email);

        //then
        assertThat(byEmail.getEmail()).isEqualTo(email);
        assertThat(byEmail.getCode()).isEqualTo("123456");
    }

}