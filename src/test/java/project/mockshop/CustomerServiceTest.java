package project.mockshop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import project.mockshop.dto.CustomerCreationDto;
import project.mockshop.dto.CustomerDto;
import project.mockshop.dto.LoginRequestDto;
import project.mockshop.entity.Address;
import project.mockshop.entity.Customer;
import project.mockshop.mapper.CustomerMapper;
import project.mockshop.repository.CustomerRepository;
import project.mockshop.service.CustomerService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceUnitTest {

    @InjectMocks
    private CustomerService customerService;
    @Mock
    private CustomerRepository customerRepository;

    private CustomerCreationDto creationDto;
    @BeforeEach
    void beforeEach() {
        creationDto = CustomerCreationDto.builder()
                .loginId("loginid")
                .name("구매자")
                .password("Password1!")
                .phoneNumber("01088888888")
                .email("email@email.com")
                .address(new Address("city", "street", "88888"))
                .build();
    }


    @Test
    void createAccount() {
        //given
        Customer customer = CustomerMapper.toEntity(creationDto);
        given(customerRepository.findByLoginId(customer.getLoginId())).willReturn(Optional.empty());
        given(customerRepository.save(any(Customer.class))).willAnswer(invocation -> {
            Customer savedCustomer = invocation.getArgument(0);
            savedCustomer.changeId(1L);
            return savedCustomer;
        });

        //when
        customerService.createAccount(creationDto);
        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));

        //then
        Customer findCustomer = customerRepository.findById(customer.getId()).orElse(null);
        if (findCustomer == null) {
            throw new NullPointerException();
        }

        assertThat(findCustomer.getName()).isEqualTo("구매자");
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void noDuplicateLoginId() {
        //given
        String duplicateLoginId = "loginid";

        //when
        when(customerRepository.findByLoginId(duplicateLoginId))
                .thenReturn(Optional.of(CustomerMapper.toEntity(creationDto)));

        //then
        assertThatThrownBy(() -> customerService.validateDuplicateLoginId(duplicateLoginId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("이미 중복된 로그인 아이디가 있습니다.");
    }

    @Test
    void login() {
        //given
        LoginRequestDto loginRequestDto = LoginRequestDto.builder().loginId("loginid").password("Password1!").build();
        given(customerRepository.findByLoginId(loginRequestDto.getLoginId()))
                .willReturn(Optional.of(CustomerMapper.toEntity(creationDto)));


        //when
        CustomerDto loginCustomer = customerService.login(loginRequestDto);

        //then
        assertThat(loginCustomer.getLoginId()).isEqualTo(loginRequestDto.getLoginId());
        verify(customerRepository, times(1)).findByLoginId("loginid");
    }

    @Test
    void loginFail_wrongLoginId() {
        //given
        customerService.createAccount(creationDto);
        LoginRequestDto loginRequestDto = LoginRequestDto.builder().loginId("wrongLoginId").password("Password1!").build();
        willThrow(new NullPointerException("아이디나 비밀번호가 일치하지 않습니다."))
                .given(customerRepository).findByLoginId(loginRequestDto.getLoginId());

        //when

        //then
        assertThatThrownBy(() -> customerService.login(loginRequestDto))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("아이디나 비밀번호가 일치하지 않습니다.");
        verify(customerRepository, times(1)).findByLoginId("wrongLoginId");
    }

    @Test
    void loginFail_wrongPassword() {
        //given
        customerService.createAccount(creationDto);
        LoginRequestDto loginRequestDto = LoginRequestDto.builder().loginId("loginId").password("password!").build();
        given(customerRepository.findByLoginId(loginRequestDto.getLoginId()))
                .willReturn(Optional.of(CustomerMapper.toEntity(creationDto)));

        //when

        //then
        assertThatThrownBy(() -> customerService.login(loginRequestDto))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("아이디나 비밀번호가 일치하지 않습니다.");
        verify(customerRepository, times(1)).findByLoginId("loginid");
    }

    @Test
    void findLoginIdByPhoneNumber() {
        //given
        given(customerRepository.findLoginIdByPhoneNumber(creationDto.getPhoneNumber()))
                .willReturn(Optional.of(CustomerMapper.toEntity(creationDto)));

        //when
        CustomerDto findCustomer = customerService.findLoginId("01088888888");

        //then
        assertThat(findCustomer.getLoginId()).isEqualTo(creationDto.getLoginId());
        verify(customerRepository, times(1)).findLoginIdByPhoneNumber("01088888888");
    }

    @Test
    void findLoginIdByPhoneNumber_fail() {
        //given
        customerService.createAccount(creationDto);
        willThrow(new NullPointerException("입력한 핸드폰 번호와 일치하는 아이디가 없습니다."))
                .given(customerRepository).findLoginIdByPhoneNumber("01011111111");

        //then
        assertThatThrownBy(() -> customerService.findLoginId("01011111111"))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("입력한 핸드폰 번호와 일치하는 아이디가 없습니다.");
        verify(customerRepository, times(1)).findLoginIdByPhoneNumber("01011111111");
    }

    @Test
    void findPassword() {
        //given
        customerService.createAccount(creationDto);
        given(customerRepository.findByLoginId(creationDto.getLoginId()))
                .willReturn(Optional.of(CustomerMapper.toEntity(creationDto)));

        //when
        CustomerDto findCustomer = customerService.findPassword("loginid", "01088888888");

        //then
        assertThat(findCustomer.getLoginId()).isEqualTo(creationDto.getLoginId());
        verify(customerRepository, times(2)).findByLoginId("loginid");
    }

    @Test
    void findPassword_fail_loginId() {
        //given
        customerService.createAccount(creationDto);
        willThrow(new NullPointerException("해당 로그인 아이디와 일치하는 정보가 없습니다."))
                .given(customerRepository).findByLoginId("nologinid");

        //then
        assertThatThrownBy(() -> customerService.findPassword("nologinid", "01088888888"))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("해당 로그인 아이디와 일치하는 정보가 없습니다.");
        verify(customerRepository, times(1)).findByLoginId("nologinid");
    }

    @Test
    void findPassword_fail_phoneNumber() {
        //given
        customerService.createAccount(creationDto);
        given(customerRepository.findByLoginId(creationDto.getLoginId()))
                .willReturn(Optional.of(CustomerMapper.toEntity(creationDto)));

        //then
        assertThatThrownBy(() -> customerService.findPassword("loginid", "01011111111"))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("해당 로그인 아이디와 입력하신 핸드폰 번호가 일치하지 않습니다.");
        verify(customerRepository, times(2)).findByLoginId("loginid");
    }

    @Test
    void findOne() {
        //given
        Customer customer = CustomerMapper.toEntity(creationDto);
        Long id = customerService.createAccount(creationDto);
        given(customerRepository.findById(id)).willReturn(Optional.of(customer));

        //when
        CustomerDto findCustomer = customerService.findOne(customer.getId());

        //then
        assertThat(findCustomer).isEqualTo(CustomerMapper.toDto(customer));
        verify(customerRepository, times(1)).findById(customer.getId());
    }

//    @Test
//    void resetPassword() {
//        //given
//        Customer customer = CustomerMapper.toEntity(creationDto);
//        customerService.createAccount(creationDto);
//
//        //when
//        customerService.resetPassword(customer, "NewPassword!1");
//
//        //then
//        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
//        Customer findCustomer = customerService.findOne(customer.getId());
//        assertThat(findCustomer.getPassword()).isEqualTo("NewPassword!1");
//        verify(customerRepository, times(1)).findById(customer.getId());
//    }
}