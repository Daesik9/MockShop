package project.mockshop;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import project.mockshop.dto.LoginRequestDto;
import project.mockshop.entity.Customer;
import project.mockshop.repository.CustomerRepository;
import project.mockshop.service.CustomerService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @InjectMocks
    private CustomerService customerService;
    @Mock
    private CustomerRepository customerRepository;


    @Test
    void createAccount() {
        //given
        Customer customer = new Customer();
        when(customerRepository.findByLoginId(customer.getLoginId())).thenReturn(Optional.empty());
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> {
            Customer savedCustomer = invocation.getArgument(0);
            savedCustomer.changeId(1L);
            return savedCustomer;
        });

        //when
        customerService.createAccount(customer);

        //then
        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
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
        Customer customer = new Customer();
        Customer duplicateCustomer = new Customer();

        //when
        customerService.createAccount(customer);

        //then
        when(customerRepository.findByLoginId(duplicateCustomer.getLoginId())).thenReturn(Optional.of(customer));
        assertThatThrownBy(() -> customerService.createAccount(duplicateCustomer))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("이미 중복된 로그인 아이디가 있습니다.");
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void login() {
        //given
        customerService.createAccount(new Customer());
        LoginRequestDto loginRequestDto = new LoginRequestDto("loginid", "Password1!");
        when(customerRepository.findByLoginId(loginRequestDto.getLoginId())).thenReturn(Optional.of(new Customer()));


        //when
        Customer loginCustomer = customerService.login(loginRequestDto);

        //then
        assertThat(loginCustomer.getLoginId()).isEqualTo(loginRequestDto.getLoginId());
        verify(customerRepository, times(2)).findByLoginId("loginid");
    }

    @Test
    void loginFail_wrongLoginId() {
        //given
        customerService.createAccount(new Customer());
        LoginRequestDto loginRequestDto = new LoginRequestDto("wrongLoginId", "Password1!");
        doThrow(new NullPointerException("아이디나 비밀번호가 일치하지 않습니다."))
                .when(customerRepository).findByLoginId(loginRequestDto.getLoginId());

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
        customerService.createAccount(new Customer());
        LoginRequestDto loginRequestDto = new LoginRequestDto("loginid", "password!");
        when(customerRepository.findByLoginId(loginRequestDto.getLoginId())).thenReturn(Optional.of(new Customer()));

        //when

        //then
        assertThatThrownBy(() -> customerService.login(loginRequestDto))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("아이디나 비밀번호가 일치하지 않습니다.");
        verify(customerRepository, times(2)).findByLoginId("loginid");
    }

    @Test
    void findLoginIdByPhoneNumber() {
        //given
        Customer customer = new Customer();
        customerService.createAccount(customer);
        when(customerRepository.findLoginIdByPhoneNumber(customer.getPhoneNumber())).thenReturn(Optional.of(customer));

        //when
        Customer findCustomer = customerService.findLoginId("01088888888");

        //then
        assertThat(findCustomer.getLoginId()).isEqualTo(customer.getLoginId());
        verify(customerRepository, times(1)).findLoginIdByPhoneNumber("01088888888");
    }

    @Test
    void findLoginIdByPhoneNumber_fail() {
        //given
        Customer customer = new Customer();
        customerService.createAccount(customer);
        doThrow(new NullPointerException("입력한 핸드폰 번호와 일치하는 아이디가 없습니다.")).when(customerRepository)
                .findLoginIdByPhoneNumber("01011111111");

        //then
        assertThatThrownBy(() -> customerService.findLoginId("01011111111"))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("입력한 핸드폰 번호와 일치하는 아이디가 없습니다.");
        verify(customerRepository, times(1)).findLoginIdByPhoneNumber("01011111111");
    }

    @Test
    void findPassword() {
        //given
        Customer customer = new Customer();
        customerService.createAccount(customer);
        when(customerRepository.findByLoginId(customer.getLoginId())).thenReturn(Optional.of(customer));

        //when
        Customer findCustomer = customerService.findPassword("loginid", "01088888888");

        //then
        assertThat(findCustomer.getLoginId()).isEqualTo(customer.getLoginId());
        verify(customerRepository, times(2)).findByLoginId("loginid");
    }

    @Test
    void findPassword_fail_loginId() {
        //given
        Customer customer = new Customer();
        customerService.createAccount(customer);
        doThrow(new NullPointerException("해당 로그인 아이디와 일치하는 정보가 없습니다.")).when(customerRepository)
                .findByLoginId("nologinid");

        //then
        assertThatThrownBy(() -> customerService.findPassword("nologinid", "01088888888"))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("해당 로그인 아이디와 일치하는 정보가 없습니다.");
        verify(customerRepository, times(1)).findByLoginId("nologinid");
    }

    @Test
    void findPassword_fail_phoneNumber() {
        //given
        Customer customer = new Customer();
        customerService.createAccount(customer);
        when(customerRepository.findByLoginId(customer.getLoginId())).thenReturn(Optional.of(customer));

        //then
        assertThatThrownBy(() -> customerService.findPassword("loginid", "01011111111"))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("해당 로그인 아이디와 입력하신 핸드폰 번호가 일치하지 않습니다.");
        verify(customerRepository, times(2)).findByLoginId("loginid");
    }

    @Test
    void findOne() {
        //given
        Customer customer = new Customer();
        customerService.createAccount(customer);
        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));

        //when
        Customer findCustomer = customerService.findOne(customer.getId());

        //then
        assertThat(findCustomer).isEqualTo(customer);
        verify(customerRepository, times(1)).findById(customer.getId());
    }

    @Test
    void resetPassword() {
        //given
        Customer customer = new Customer();
        customerService.createAccount(customer);

        //when
        customerService.resetPassword(customer, "NewPassword!1");

        //then
        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        Customer findCustomer = customerService.findOne(customer.getId());
        assertThat(findCustomer.getPassword()).isEqualTo("NewPassword!1");
        verify(customerRepository, times(1)).findById(customer.getId());
    }
}