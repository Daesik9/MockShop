package project.mockshop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import project.mockshop.dto.CustomerCreationDto;
import project.mockshop.dto.CustomerDto;
import project.mockshop.dto.UpdateProfileDto;
import project.mockshop.entity.Address;
import project.mockshop.mapper.CustomerMapper;
import project.mockshop.policy.CustomerPolicy;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
public class CustomerServiceSpringTest {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    Long userId;

    @BeforeEach
    void beforeEach() {
        CustomerCreationDto creationDto = CustomerCreationDto.builder()
                .loginId("loginid")
                .name("구매자")
                .password("Password1!")
                .phoneNumber("01088888888")
                .email("email@email.com")
                .address(new Address("city", "street", "88888"))
                .build();
        userId = customerService.createAccount(creationDto);
    }

    @Test
    void notNull() throws Exception {
        //given

        //when

        //then
        assertThat(customerService).isNotNull();
    }

    /**
     * 비밀번호, 이름, 핸드폰번호, 이메일주소, 주소만 변경 가능하다.
     */
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
        customerService.updateProfile(updateProfileDto);

        //then
        CustomerDto customerDto = customerService.findOne(updateProfileDto.getUserId());
        assertThat(customerDto.getName()).isEqualTo("새이름");
        assertThat(customerDto.getPassword()).isEqualTo("Newpassword1!");
        assertThat(customerDto.getEmail()).isEqualTo("newemail@gmail.com");
        assertThat(customerDto.getPhoneNumber()).isEqualTo("01022222222");
        assertThat(customerDto.getAddress().getCity()).isEqualTo("new city");
    }

    @Test
    void updateProfile_fail_samePassword() throws Exception {
        //given
        UpdateProfileDto updateProfileDto = UpdateProfileDto.builder()
                .userId(userId)
                .name("새이름")
                .password("Password1!")
                .email("newemail@gmail.com")
                .phoneNumber("01022222222")
                .address(new Address("new city", "new street", "99999"))
                .build();

        //when
        assertThatThrownBy(() -> customerService.updateProfile(updateProfileDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CustomerPolicy.SAME_PASSWORD_STRING);

        //then
        CustomerDto customerDto = customerService.findOne(updateProfileDto.getUserId());
        assertThat(customerDto.getName()).isEqualTo("구매자");
        assertThat(passwordEncoder.matches("Password1!", customerDto.getPassword())).isTrue();
        assertThat(customerDto.getEmail()).isEqualTo("email@email.com");
        assertThat(customerDto.getPhoneNumber()).isEqualTo("01088888888");
        assertThat(customerDto.getAddress().getCity()).isEqualTo("city");
    }


}