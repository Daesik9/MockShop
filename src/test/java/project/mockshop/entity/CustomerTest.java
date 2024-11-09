package project.mockshop.entity;

import org.junit.jupiter.api.Test;
import project.mockshop.policy.CustomerPolicy;
import project.mockshop.policy.MockShopPolicy;

import static org.assertj.core.api.Assertions.*;

/**
 * 구매자는 id, 로그인 아이디, 비밀번호, 이름, 전화번호, 이메일,
 * 주소, 포인트, 삭제 여부가 있어야한다.
 */
public class CustomerTest {

    @Test
    void customerIsNotNull() {
        Customer customer = Customer.builder().build();
        assertThat(customer).isNotNull();
    }

    @Test
    void hasId() {
        Customer customer2 = Customer.builder().id(0L).build();
        assertThat(customer2.getId()).isEqualTo(0L);
    }

    @Test
    void hasLoginId() {
        assertThatThrownBy(() -> Customer.builder().loginId("  ").build())
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining(MockShopPolicy.INPUT_STRING_METHOD("로그인 id"));

        Customer customer = Customer.builder().loginId("loginid").build();
        assertThat(customer.getLoginId()).isEqualTo("loginid");
    }

    /**
     * Customer.CustomerPolicy.LOGIN_ID_LENGTH_STRING
     * 로그인 아이디는 3-15자리로 구성되어야 합니다.
     */
    @Test
    void loginIdLength3to15() {
        assertThatThrownBy(() -> Customer.builder().loginId("a1").build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CustomerPolicy.LOGIN_ID_LENGTH_STRING);

        assertThatThrownBy(() -> Customer.builder().loginId("a1234567890123456").build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CustomerPolicy.LOGIN_ID_LENGTH_STRING);

        assertThatCode(() -> Customer.builder().loginId("a12345").build()).doesNotThrowAnyException();
    }

    /**
     * Customer.CustomerPolicy.LOGIN_ID_POLICY_STRING
     * 로그인 아이디는 소문자와 숫자, -, _로만 구성되어야 합니다.
     */
    @Test
    void loginOnlyLetterNumberDashUnderscore() {
        assertThatThrownBy(() -> Customer.builder().loginId("  a  ").build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CustomerPolicy.LOGIN_ID_POLICY_STRING);

        assertThatThrownBy(() -> Customer.builder().loginId("!@#$!#%").build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CustomerPolicy.LOGIN_ID_POLICY_STRING);

        assertThatThrownBy(() -> Customer.builder().loginId("ASDF").build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CustomerPolicy.LOGIN_ID_POLICY_STRING);

        assertThatCode(() -> Customer.builder().loginId("asdf123-_").build()).doesNotThrowAnyException();
    }

    @Test
    void hasPassword() {
        assertThatThrownBy(() -> Customer.builder().loginId("loginid").name("이름").password("").build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(MockShopPolicy.INPUT_STRING_METHOD("비밀번호"));

        assertThatCode(() -> Customer.builder().loginId("loginid").name("이름").password("Password1!").build())
                .doesNotThrowAnyException();

        Customer customer = Customer.builder().loginId("loginid").name("이름").password("Password1!").build();
        assertThat(customer.getPassword()).isEqualTo("Password1!");
    }

    /**
     * Customer.CustomerPolicy.MIN_PASSWORD_STRING
     * 비밀번호는 최소 8자 이상이어야 합니다.
     */
    @Test
    void passwordLengthMinimum8() {
        assertThatThrownBy(() -> Customer.builder().loginId("loginid").name("이름").password("pass").build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CustomerPolicy.MIN_PASSWORD_STRING);
    }

    /**
     * Customer.CustomerPolicy.MAX_PASSWORD_STRING
     * 비밀번호는 최대 128자 이하여야 합니다.
     */
    @Test
    void passwordLengthMaximum128() {
        String password = "passwordpasswordpasswordpasswordpasswordpasswordpasswordpassword" +
                "passwordpasswordpasswordpasswordpasswordpasswordpasswordpasswordpassword";
        assertThatThrownBy(() -> Customer.builder().loginId("loginid").name("이름").password(password).build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CustomerPolicy.MAX_PASSWORD_STRING);
    }

    /**
     * Customer.CustomerPolicy.PASSWORD_POLICY_STRING
     * 비밀번호는 대소문자, 특수문자, 숫자가 포함되어야 합니다.
     */
    @Test
    void passwordPolicy() {
        //소문자
        assertThatThrownBy(() -> Customer.builder().loginId("loginid").name("이름").password("password").build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CustomerPolicy.PASSWORD_POLICY_STRING);

        //대문자
        assertThatThrownBy(() -> Customer.builder().loginId("loginid").name("이름").password("PASSWORD").build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CustomerPolicy.PASSWORD_POLICY_STRING);

        //숫자
        assertThatThrownBy(() -> Customer.builder().loginId("loginid").name("이름").password("12345678").build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CustomerPolicy.PASSWORD_POLICY_STRING);

        //특수문자
        assertThatThrownBy(() -> Customer.builder().loginId("loginid").name("이름").password("!@#$%^&*").build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CustomerPolicy.PASSWORD_POLICY_STRING);

        //소문자, 대문자
        assertThatThrownBy(() -> Customer.builder().loginId("loginid").name("이름").password("passWORD").build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CustomerPolicy.PASSWORD_POLICY_STRING);

        //소문자, 숫자
        assertThatThrownBy(() -> Customer.builder().loginId("loginid").name("이름").password("pass1111").build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CustomerPolicy.PASSWORD_POLICY_STRING);

        //소문자, 특수문자
        assertThatThrownBy(() -> Customer.builder().loginId("loginid").name("이름").password("pass!@#$").build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CustomerPolicy.PASSWORD_POLICY_STRING);

        //대문자, 숫자
        assertThatThrownBy(() -> Customer.builder().loginId("loginid").name("이름").password("PASS1234").build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CustomerPolicy.PASSWORD_POLICY_STRING);

        //대문자, 특수문자
        assertThatThrownBy(() -> Customer.builder().loginId("loginid").name("이름").password("PASS!@#$").build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CustomerPolicy.PASSWORD_POLICY_STRING);

        //숫자, 특수문자
        assertThatThrownBy(() -> Customer.builder().loginId("loginid").name("이름").password("1234!@#$").build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CustomerPolicy.PASSWORD_POLICY_STRING);

        //소문자, 대문자, 숫자
        assertThatThrownBy(() -> Customer.builder().loginId("loginid").name("이름").password("paSS1234").build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CustomerPolicy.PASSWORD_POLICY_STRING);

        //소문자, 대문자, 특수문자
        assertThatThrownBy(() -> Customer.builder().loginId("loginid").name("이름").password("paSS!@#$").build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CustomerPolicy.PASSWORD_POLICY_STRING);

        //소문자, 숫자, 특수문자
        assertThatThrownBy(() -> Customer.builder().loginId("loginid").name("이름").password("pa11!@#$").build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CustomerPolicy.PASSWORD_POLICY_STRING);

        //대문자, 숫자, 특수문자
        assertThatThrownBy(() -> Customer.builder().loginId("loginid").name("이름").password("PASS12!@#$").build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CustomerPolicy.PASSWORD_POLICY_STRING);

        //소문자, 대문자, 숫자, 특수문자 -> 오류 X
        assertThatCode(() -> Customer.builder().loginId("loginid").name("이름").password("paSS12!@#$").build())
                .doesNotThrowAnyException();

        Customer customer = Customer.builder().loginId("loginid").name("이름").password("paSS12!@#$").build();
        assertThat(customer.getPassword()).isEqualTo("paSS12!@#$");
    }

    @Test
    void hasName() {
        assertThatThrownBy(() -> Customer.builder().loginId("loginid").name("").build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(MockShopPolicy.INPUT_STRING_METHOD("이름"));

        assertThatCode(() -> Customer.builder().loginId("loginid").name("이름").build())
                .doesNotThrowAnyException();

        Customer customer = Customer.builder().loginId("loginid").name("이름").build();
        assertThat(customer.getName()).isEqualTo("이름");
    }

    /**
     * Customer.CustomerPolicy.NAME_LENGTH_STRING
     * 이름은 2-15자로 구성되어야합니다.
     */
    @Test
    void nameLength2to15() {
        assertThatThrownBy(() -> Customer.builder().loginId("loginid").name("이").build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CustomerPolicy.NAME_LENGTH_STRING);

        assertThatThrownBy(() -> Customer.builder().loginId("loginid").name("이름이름이름이름이름이름이름이름").build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CustomerPolicy.NAME_LENGTH_STRING);

        assertThatCode(() -> Customer.builder().loginId("loginid").name("이름").build())
                .doesNotThrowAnyException();
    }

    /**
     * Customer.CustomerPolicy.NAME_POLICY_STRING
     * 이름은 한글로만 구성되어야합니다.
     */
    @Test
    void nameOnlyKorean() {
        assertThatThrownBy(() -> Customer.builder().loginId("loginid").name("name"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CustomerPolicy.NAME_POLICY_STRING);

        assertThatCode(() -> Customer.builder().loginId("loginid").name("이름").build())
                .doesNotThrowAnyException();
    }

    /**
     * 전화번호 xxx-xxxx-xxxx 포맷에 맞게 -를 제외한 숫자로만 구성되어야 합니다.
     */
    @Test
    void phoneNumberOnlyDigitAndFormat() {
        assertThatThrownBy(() ->
                Customer.builder().loginId("loginid").name("이름").password("Password1!").phoneNumber("asdfasdf").build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CustomerPolicy.PHONENUMBER_POLICY_STRING);

        assertThatThrownBy(() ->
                Customer.builder().loginId("loginid").name("이름").password("Password1!").phoneNumber("010777").build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CustomerPolicy.PHONENUMBER_POLICY_STRING);

        Customer customer = Customer.builder().loginId("loginid").name("이름")
                .password("Password1!").phoneNumber("01077777777").build();
        assertThat(customer.getPhoneNumber()).isEqualTo("01077777777");
    }

    /**
     * 이메일 형식을 다시 확인해주세요.
     */
    @Test
    void emailFormat() {
        assertThatThrownBy(() -> Customer.builder().loginId("loginid").name("이름").password("Password1!")
                .phoneNumber("01011111111").email("email").build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CustomerPolicy.EMAIL_POLICY_STRING);

        Customer customer = Customer.builder().loginId("loginid").name("이름").password("Password1!")
                .phoneNumber("01011111111").email("email@email.com").build();
        assertThat(customer.getEmail()).isEqualTo("email@email.com");
    }

    /**
     * Address는 address, detailedAddress, zonecode 구성되어 있고,
     * zonecode는 숫자로만 구성되어야 한다.
     */
    @Test
    void address() {
        assertThatThrownBy(() -> new AddressInfo("city", "street", "zonecode"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CustomerPolicy.ZONECODE_POLICY_STRING);

        AddressInfo addressInfo = new AddressInfo("city", "street", "88888");
        Customer customer = Customer.builder().loginId("loginid").name("이름").password("Password1!")
                .phoneNumber("01011111111").email("email@email.com").addressInfo(addressInfo).build();
        assertThat(customer.getAddressInfo().getAddress()).isEqualTo("city");
        assertThat(customer.getAddressInfo().getDetailedAddress()).isEqualTo("street");
        assertThat(customer.getAddressInfo().getZonecode()).isEqualTo("88888");
    }

    /**
     * 포인트는 음수일 수 없습니다.
     */
    @Test
    void point() {
        AddressInfo addressInfo = new AddressInfo("city", "street", "88888");


        assertThatThrownBy(() -> Customer.builder().loginId("loginid").name("이름").password("Password1!")
                .phoneNumber("01011111111").email("email@email.com").addressInfo(addressInfo).point(-100).build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CustomerPolicy.POINT_POLICY_STRING);

        Customer customer = Customer.builder().loginId("loginid").name("이름").password("Password1!")
                .phoneNumber("01011111111").email("email@email.com").addressInfo(addressInfo).point(100).build();

        assertThat(customer.getPoint()).isEqualTo(100);
    }

    @Test
    void isDeleted() {
        AddressInfo addressInfo = new AddressInfo("city", "street", "88888");
        Customer customer = Customer.builder().loginId("loginid").name("이름").password("Password1!")
                .phoneNumber("01011111111").email("email@email.com").addressInfo(addressInfo).point(100).isDeleted(false).build();

        assertThat(customer.isDeleted()).isFalse();
    }
}