package project.mockshop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.mockshop.dto.LoginRequestDto;
import project.mockshop.entity.Customer;
import project.mockshop.policy.CustomerPolicy;
import project.mockshop.repository.CustomerRepository;
import project.mockshop.service.CustomerService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

public class CustomerServiceTest {

    private CustomerService customerService;
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        customerRepository = new CustomerRepository();
        customerService = new CustomerService(customerRepository);
    }

    @Test
    void createAccount() {
        //given
        Customer customer = new Customer();

        //when
        customerService.createAccount(customer);

        //then
        Customer findCustomer = customerRepository.findById(customer.getId()).orElse(null);
        if (findCustomer == null) {
            throw new NullPointerException();
        }

        assertThat(findCustomer.getName()).isEqualTo("구매자");
    }

    @Test
    void noDuplicateLoginId() {
        //given
        Customer customer = new Customer();
        Customer duplicateCustomer = new Customer();

        //when
        customerService.createAccount(customer);

        //then
        assertThatThrownBy(() -> customerService.createAccount(duplicateCustomer))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("이미 중복된 로그인 아이디가 있습니다.");
    }

    @Test
    void login() {
        //given
        customerService.createAccount(new Customer());
        LoginRequestDto loginRequestDto = new LoginRequestDto("loginid", "Password1!");

        //when
        Customer loginCustomer = customerService.login(loginRequestDto);

        //then
        assertThat(loginCustomer.getLoginId()).isEqualTo(loginRequestDto.getLoginId());
    }

    @Test
    void loginFail_wrongLoginId() {
        //given
        customerService.createAccount(new Customer());
        LoginRequestDto loginRequestDto = new LoginRequestDto("wrongLoginId", "Password1!");

        //when

        //then
        assertThatThrownBy(() -> customerService.login(loginRequestDto))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("아이디나 비밀번호가 일치하지 않습니다.");
    }

    @Test
    void loginFail_wrongPassword() {
        //given
        customerService.createAccount(new Customer());
        LoginRequestDto loginRequestDto = new LoginRequestDto("loginid", "password!");

        //when

        //then
        assertThatThrownBy(() -> customerService.login(loginRequestDto))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("아이디나 비밀번호가 일치하지 않습니다.");
    }

    @Test
    void findLoginIdByPhoneNumber() {
        //given
        Customer customer = new Customer();
        customerService.createAccount(customer);

        //when
        Customer findCustomer = customerService.findLoginId("01088888888");

        //then
        assertThat(findCustomer.getLoginId()).isEqualTo(customer.getLoginId());
    }

    @Test
    void findLoginIdByPhoneNumber_fail() {
        //given
        Customer customer = new Customer();
        customerService.createAccount(customer);

        //then
        assertThatThrownBy(() -> customerService.findLoginId("01011111111"))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("입력한 핸드폰 번호와 일치하는 아이디가 없습니다.");
    }

    @Test
    void findPassword() {
        //given
        Customer customer = new Customer();
        customerService.createAccount(customer);

        //when
        Customer findCustomer = customerService.findPassword("loginid", "01088888888");

        //then
        assertThat(findCustomer.getLoginId()).isEqualTo(customer.getLoginId());
    }

    @Test
    void findPassword_fail_loginId() {
        //given
        Customer customer = new Customer();
        customerService.createAccount(customer);

        //then
        assertThatThrownBy(() -> customerService.findPassword("nologinid", "01088888888"))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("해당 로그인 아이디와 일치하는 정보가 없습니다.");
    }

    @Test
    void findPassword_fail_phoneNumber() {
        //given
        Customer customer = new Customer();
        customerService.createAccount(customer);

        //then
        assertThatThrownBy(() -> customerService.findPassword("loginid", "01011111111"))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("해당 로그인 아이디와 입력하신 핸드폰 번호가 일치하지 않습니다.");
    }

    @Test
    void findOne() {
        //given
        Customer customer = new Customer();
        customerService.createAccount(customer);

        //when
        Customer findCustomer = customerService.findOne(customer.getId());

        //then
        assertThat(findCustomer).isEqualTo(customer);
    }

    @Test
    void resetPassword() {
        //given
        Customer customer = new Customer();
        customerService.createAccount(customer);

        //when
        customerService.resetPassword(customer, "NewPassword!1");

        //then
        Customer findCustomer = customerService.findOne(customer.getId());
        assertThat(findCustomer.getPassword()).isEqualTo("NewPassword!1");
    }
}