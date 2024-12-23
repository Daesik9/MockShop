package project.mockshop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import project.mockshop.dto.CustomerCreationDto;
import project.mockshop.dto.CustomerDto;
import project.mockshop.dto.LoginRequestDto;
import project.mockshop.dto.UpdateProfileDto;
import project.mockshop.entity.AddressInfo;
import project.mockshop.entity.Customer;
import project.mockshop.mapper.CustomerMapper;
import project.mockshop.policy.CustomerPolicy;
import project.mockshop.repository.CustomerRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @InjectMocks
    private CustomerService customerService;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private CustomerDto customerDto;
    @BeforeEach
    void beforeEach() {
        customerDto = CustomerDto.builder()
                .id(1L)
                .loginId("loginid")
                .name("구매자")
                .password("Password1!")
                .phoneNumber("01088888888")
                .email("email@email.com")
                .addressInfo(new AddressInfo("city", "street", "88888"))
                .build();
    }


    @Test
    void createAccount() {
        //given
        CustomerCreationDto creationDto = CustomerCreationDto.builder()
                .loginId("loginid")
                .name("구매자")
                .password("Password1!")
                .phoneNumber("01088888888")
                .email("email@email.com")
                .addressInfo(new AddressInfo("city", "street", "88888"))
                .build();
        Customer customer = CustomerMapper.toEntity(customerDto);
        given(customerRepository.findByLoginId(customer.getLoginId())).willReturn(Optional.empty());
        given(customerRepository.save(any(Customer.class))).willAnswer(invocation -> {
            Customer savedCustomer = invocation.getArgument(0);
//            savedCustomer.changeId(1L);
            return savedCustomer;
        });
        given(bCryptPasswordEncoder.encode(customer.getPassword())).willReturn("encodedPassword1!");

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
                .thenReturn(Optional.of(CustomerMapper.toEntity(customerDto)));

        //then
        assertThat(customerService.validateDuplicateLoginId(duplicateLoginId)).isTrue();
    }

    @Test
    void login() {
        //given
        LoginRequestDto loginRequestDto = LoginRequestDto.builder().loginId("loginid").password("Password1!").build();
        given(customerRepository.findByLoginId(loginRequestDto.getLoginId()))
                .willReturn(Optional.of(CustomerMapper.toEntity(customerDto)));

        //when
        CustomerDto loginCustomer = customerService.login(loginRequestDto);

        //then
        assertThat(loginCustomer.getLoginId()).isEqualTo(loginRequestDto.getLoginId());
        verify(customerRepository, times(1)).findByLoginId("loginid");
    }

    @Test
    void loginFail_wrongLoginId() {
        //given
        LoginRequestDto loginRequestDto = LoginRequestDto.builder().loginId("wrongloginid").password("Password1!").build();
        willThrow(new NullPointerException("아이디나 비밀번호가 일치하지 않습니다."))
                .given(customerRepository).findByLoginId(loginRequestDto.getLoginId());

        //when

        //then
        assertThatThrownBy(() -> customerService.login(loginRequestDto))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("아이디나 비밀번호가 일치하지 않습니다.");
        verify(customerRepository, times(1)).findByLoginId("wrongloginid");
    }

    @Test
    void loginFail_wrongPassword() {
        //given
        LoginRequestDto loginRequestDto = LoginRequestDto.builder().loginId("loginid").password("password!").build();
        given(customerRepository.findByLoginId(loginRequestDto.getLoginId()))
                .willReturn(Optional.of(CustomerMapper.toEntity(customerDto)));

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
        given(customerRepository.findByPhoneNumber(customerDto.getPhoneNumber()))
                .willReturn(Optional.of(CustomerMapper.toEntity(customerDto)));

        //when
        CustomerDto findCustomer = customerService.findLoginIdByPhoneNumber("01088888888");

        //then
        assertThat(findCustomer.getLoginId()).isEqualTo(customerDto.getLoginId());
        verify(customerRepository, times(1)).findByPhoneNumber("01088888888");
    }

    @Test
    void findLoginIdByPhoneNumber_fail() {
        //given
        willThrow(new NullPointerException("입력한 핸드폰 번호와 일치하는 아이디가 없습니다."))
                .given(customerRepository).findByPhoneNumber("01011111111");

        //then
        assertThatThrownBy(() -> customerService.findLoginIdByPhoneNumber("01011111111"))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("입력한 핸드폰 번호와 일치하는 아이디가 없습니다.");
        verify(customerRepository, times(1)).findByPhoneNumber("01011111111");
    }

    @Test
    void findLoginIdByEmail() {
        //given
        given(customerRepository.findByEmail(customerDto.getEmail()))
                .willReturn(Optional.of(CustomerMapper.toEntity(customerDto)));

        //when
        String loginId = customerService.findLoginIdByEmail("email@email.com");

        //then
        assertThat(loginId).isEqualTo(customerDto.getLoginId());
        verify(customerRepository, times(1)).findByEmail("email@email.com");
    }

    @Test
    void findLoginIdByEmail_fail() {
        //given
        willThrow(new NullPointerException("일치하는 아이디가 없습니다."))
                .given(customerRepository).findByEmail("noemail@email.com");

        //then
        assertThatThrownBy(() -> customerService.findLoginIdByEmail("noemail@email.com"))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("일치하는 아이디가 없습니다.");
        verify(customerRepository, times(1)).findByEmail("noemail@email.com");
    }

    @Test
    void findPassword() {
        //given
        given(customerRepository.findByLoginId(customerDto.getLoginId()))
                .willReturn(Optional.of(CustomerMapper.toEntity(customerDto)));

        //when
        CustomerDto findCustomer = customerService.findPassword("loginid", "01088888888");

        //then
        assertThat(findCustomer.getLoginId()).isEqualTo(customerDto.getLoginId());
        verify(customerRepository, times(1)).findByLoginId("loginid");
    }

    @Test
    void findPassword_fail_loginId() {
        //given
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
        given(customerRepository.findByLoginId(customerDto.getLoginId()))
                .willReturn(Optional.of(CustomerMapper.toEntity(customerDto)));

        //then
        assertThatThrownBy(() -> customerService.findPassword("loginid", "01011111111"))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("해당 로그인 아이디와 입력하신 핸드폰 번호가 일치하지 않습니다.");
        verify(customerRepository, times(1)).findByLoginId("loginid");
    }

    @Test
    void findOne() {
        //given
        Customer customer = CustomerMapper.toEntity(customerDto);
        given(customerRepository.findById(customer.getId())).willReturn(Optional.of(customer));

        //when
        CustomerDto findCustomer = customerService.findOne(customer.getId());

        //then
        assertThat(findCustomer.getLoginId()).isEqualTo(customerDto.getLoginId());
        verify(customerRepository, times(1)).findById(customer.getId());
    }

    @Test
    void resetPassword() {
        //given
        given(customerRepository.findByEmail(customerDto.getEmail()))
                .willReturn(Optional.of(CustomerMapper.toEntity(customerDto)));
        given(bCryptPasswordEncoder.encode("NewPassword1!")).willReturn("EncodedPassword1!");

        //when
        customerService.resetPassword(customerDto.getEmail(), "NewPassword1!");

        //then
        Optional<Customer> findCustomer = customerRepository.findByEmail(customerDto.getEmail());
        assertThat(findCustomer.get().getPassword()).isEqualTo("EncodedPassword1!");
        verify(customerRepository, times(2)).findByEmail(customerDto.getEmail());
    }

    /**
     * 비밀번호, 이름, 핸드폰번호, 이메일주소, 주소만 변경 가능하다.
     */
    @Test
    void updateProfile() throws Exception {
        //given
        CustomerDto customer = CustomerDto.builder()
                .loginId("loginid")
                .name("이름")
                .password("Password1!")
                .email("email@gmail.com")
                .phoneNumber("01011111111")
                .addressInfo(new AddressInfo("city", "street", "11111"))
                .build();

        given(customerRepository.findById(1L)).willReturn(Optional.of(CustomerMapper.toEntity(customer)));

        UpdateProfileDto updateProfileDto = UpdateProfileDto.builder()
                .userId(1L)
                .name("새이름")
                .password("Newpassword1!")
                .email("newemail@gmail.com")
                .phoneNumber("01022222222")
                .addressInfo(new AddressInfo("new city", "new street", "99999"))
                .build();

        //when
        customerService.updateProfile(updateProfileDto);
        CustomerDto newCustomer = CustomerDto.builder()
                .loginId("loginid")
                .name("새이름")
                .password("Newpassword1!")
                .email("newemail@gmail.com")
                .phoneNumber("01022222222")
                .addressInfo(new AddressInfo("new city", "new street", "99999"))
                .build();
        when(customerRepository.findById(1L)).thenReturn(Optional.of(CustomerMapper.toEntity(newCustomer)));

        //then
        CustomerDto customerDto = customerService.findOne(updateProfileDto.getUserId());
        assertThat(customerDto.getName()).isEqualTo("새이름");
    }

    @Test
    void updateProfile_fail_samePassword() throws Exception {
        //given
        CustomerDto customer = CustomerDto.builder()
                .loginId("loginid")
                .name("이름")
                .password("Password1!")
                .email("email@gmail.com")
                .phoneNumber("01011111111")
                .addressInfo(new AddressInfo("city", "street", "11111"))
                .build();
        given(customerRepository.findById(1L)).willReturn(Optional.of(CustomerMapper.toEntity(customer)));
        UpdateProfileDto updateProfileDto = UpdateProfileDto.builder()
                .userId(1L)
                .name("새이름")
                .password("Password1!")
                .email("newemail@gmail.com")
                .phoneNumber("01022222222")
                .addressInfo(new AddressInfo("new city", "new street", "99999"))
                .build();

        //when
        when(bCryptPasswordEncoder.matches(updateProfileDto.getPassword(), customer.getPassword())).thenReturn(true);
        assertThatThrownBy(() -> customerService.updateProfile(updateProfileDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CustomerPolicy.SAME_PASSWORD_STRING);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(CustomerMapper.toEntity(customer)));

        //then
        CustomerDto customerDto = customerService.findOne(updateProfileDto.getUserId());
        assertThat(customerDto.getName()).isEqualTo("이름");
    }


}