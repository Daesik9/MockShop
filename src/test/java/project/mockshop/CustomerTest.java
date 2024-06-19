package project.mockshop;

import org.junit.jupiter.api.Test;
import project.mockshop.entity.Address;
import project.mockshop.entity.Customer;
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
        Customer customer = new Customer();
        assertThat(customer).isNotNull();
    }

    @Test
    void hasId() {
        Customer customer2 = new Customer(0L);
        assertThat(customer2.getId()).isEqualTo(0L);
    }

    @Test
    void hasLoginId() {
        assertThatThrownBy(() -> new Customer(""))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining(MockShopPolicy.INPUT_STRING_METHOD("로그인 id"));

        Customer customer = new Customer("loginid");
        assertThat(customer.getLoginId()).isEqualTo("loginid");
    }

    /**
     * Customer.CustomerPolicy.LOGIN_ID_LENGTH_STRING
     * 로그인 아이디는 3-15자리로 구성되어야 합니다.
     */
    @Test
    void loginIdLength3to15() {
        assertThatThrownBy(() -> new Customer("12"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CustomerPolicy.LOGIN_ID_LENGTH_STRING);

        assertThatThrownBy(() -> new Customer("1234567890123456"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CustomerPolicy.LOGIN_ID_LENGTH_STRING);

        assertThatCode(() -> new Customer("12345")).doesNotThrowAnyException();
    }

    /**
     * Customer.CustomerPolicy.LOGIN_ID_POLICY_STRING
     * 로그인 아이디는 소문자와 숫자, -, _로만 구성되어야 합니다.
     */
    @Test
    void loginOnlyLetterNumberDashUnderscore() {
        assertThatThrownBy(() -> new Customer("  a  "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CustomerPolicy.LOGIN_ID_POLICY_STRING);

        assertThatThrownBy(() -> new Customer("!@#$!#%"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CustomerPolicy.LOGIN_ID_POLICY_STRING);

        assertThatThrownBy(() -> new Customer("ASDF"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CustomerPolicy.LOGIN_ID_POLICY_STRING);

        assertThatCode(() -> new Customer("asdf123-_")).doesNotThrowAnyException();
    }

    @Test
    void hasPassword() {
        assertThatThrownBy(() -> new Customer("loginid", "이름", ""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(MockShopPolicy.INPUT_STRING_METHOD("비밀번호"));

        assertThatCode(() -> new Customer("loginid", "이름", "Password1!"))
                .doesNotThrowAnyException();

        Customer customer = new Customer("loginid", "이름", "Password1!");
        assertThat(customer.getPassword()).isEqualTo("Password1!");
    }

    /**
     * Customer.CustomerPolicy.MIN_PASSWORD_STRING
     * 비밀번호는 최소 8자 이상이어야 합니다.
     */
    @Test
    void passwordLengthMinimum8() {
        assertThatThrownBy(() -> new Customer("loginid", "이름", "pass"))
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
        assertThatThrownBy(() -> new Customer("loginid", "이름", password))
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
        assertThatThrownBy(() -> new Customer("loginid", "이름", "password"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CustomerPolicy.PASSWORD_POLICY_STRING);

        //대문자
        assertThatThrownBy(() -> new Customer("loginid", "이름", "PASSWORD"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CustomerPolicy.PASSWORD_POLICY_STRING);

        //숫자
        assertThatThrownBy(() -> new Customer("loginid", "이름", "12345678"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CustomerPolicy.PASSWORD_POLICY_STRING);

        //특수문자
        assertThatThrownBy(() -> new Customer("loginid", "이름", "!@#$%^&*"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CustomerPolicy.PASSWORD_POLICY_STRING);

        //소문자, 대문자
        assertThatThrownBy(() -> new Customer("loginid", "이름", "passWORD"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CustomerPolicy.PASSWORD_POLICY_STRING);

        //소문자, 숫자
        assertThatThrownBy(() -> new Customer("loginid", "이름", "pass1111"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CustomerPolicy.PASSWORD_POLICY_STRING);

        //소문자, 특수문자
        assertThatThrownBy(() -> new Customer("loginid", "이름", "pass!@#$"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CustomerPolicy.PASSWORD_POLICY_STRING);

        //대문자, 숫자
        assertThatThrownBy(() -> new Customer("loginid", "이름", "PASS1234"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CustomerPolicy.PASSWORD_POLICY_STRING);

        //대문자, 특수문자
        assertThatThrownBy(() -> new Customer("loginid", "이름", "PASS!@#$"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CustomerPolicy.PASSWORD_POLICY_STRING);

        //숫자, 특수문자
        assertThatThrownBy(() -> new Customer("loginid", "이름", "1234!@#$"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CustomerPolicy.PASSWORD_POLICY_STRING);

        //소문자, 대문자, 숫자
        assertThatThrownBy(() -> new Customer("loginid", "이름", "paSS1234"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CustomerPolicy.PASSWORD_POLICY_STRING);

        //소문자, 대문자, 특수문자
        assertThatThrownBy(() -> new Customer("loginid", "이름", "paSS!@#$"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CustomerPolicy.PASSWORD_POLICY_STRING);

        //소문자, 숫자, 특수문자
        assertThatThrownBy(() -> new Customer("loginid", "이름", "pa11!@#$"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CustomerPolicy.PASSWORD_POLICY_STRING);

        //대문자, 숫자, 특수문자
        assertThatThrownBy(() -> new Customer("loginid", "이름", "PASS12!@#$"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CustomerPolicy.PASSWORD_POLICY_STRING);

        //소문자, 대문자, 숫자, 특수문자 -> 오류 X
        assertThatCode(() -> new Customer("loginid", "이름", "paSS12!@#$"))
                .doesNotThrowAnyException();

        Customer customer = new Customer("loginid", "이름", "paSS12!@#$");
        assertThat(customer.getPassword()).isEqualTo("paSS12!@#$");
    }

    @Test
    void hasName() {
        assertThatThrownBy(() -> new Customer("loginid", ""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(MockShopPolicy.INPUT_STRING_METHOD("이름"));

        assertThatCode(() -> new Customer("loginid", "이름"))
                .doesNotThrowAnyException();

        Customer customer = new Customer("loginid", "이름");
        assertThat(customer.getName()).isEqualTo("이름");
    }

    /**
     * Customer.CustomerPolicy.NAME_LENGTH_STRING
     * 이름은 2-15자로 구성되어야합니다.
     */
    @Test
    void nameLength2to15() {
        assertThatThrownBy(() -> new Customer("loginid", "이"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CustomerPolicy.NAME_LENGTH_STRING);

        assertThatThrownBy(() -> new Customer("loginid", "이름이름이름이름이름이름이름이름"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CustomerPolicy.NAME_LENGTH_STRING);

        assertThatCode(() -> new Customer("loginid", "이름"))
                .doesNotThrowAnyException();
    }

    /**
     * Customer.CustomerPolicy.NAME_POLICY_STRING
     * 이름은 한글로만 구성되어야합니다.
     */
    @Test
    void nameOnlyKorean() {
        assertThatThrownBy(() -> new Customer("loginid", "name"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CustomerPolicy.NAME_POLICY_STRING);

        assertThatCode(() -> new Customer("loginid", "이름"))
                .doesNotThrowAnyException();
    }

    /**
     * 전화번호 xxx-xxxx-xxxx 포맷에 맞게 -를 제외한 숫자로만 구성되어야 합니다.
     */
    @Test
    void phoneNumberOnlyDigitAndFormat() {
        assertThatThrownBy(() -> new Customer("loginid", "이름", "Password1!", "asdfasdf"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CustomerPolicy.PHONENUMBER_POLICY_STRING);

        assertThatThrownBy(() -> new Customer("loginid", "이름", "Password1!", "010777"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CustomerPolicy.PHONENUMBER_POLICY_STRING);

        Customer customer = new Customer("loginid", "이름", "Password1!", "01077777777");
        assertThat(customer.getPhoneNumber()).isEqualTo("01077777777");
    }

    /**
     * 이메일 형식을 다시 확인해주세요.
     */
    @Test
    void emailFormat() {
        assertThatThrownBy(() -> new Customer("loginid", "이름", "Password1!",
                "01011111111", "email"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CustomerPolicy.EMAIL_POLICY_STRING);

        Customer customer = new Customer("loginid", "이름", "Password1!",
                "01011111111", "email@email.com");
        assertThat(customer.getEmail()).isEqualTo("email@email.com");
    }

    /**
     * Address는 city, street, zipcode로 구성되어 있고,
     * zipcode는 숫자로만 구성되어야 한다.
     */

    @Test
    void address() {
        assertThatThrownBy(() -> new Address("city", "street", "zipcode"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CustomerPolicy.ZIPCODE_POLICY_STRING);

        Address address = new Address("city", "street", "88888");
        Customer customer = new Customer("loginid", "이름", "Password1!",
                "01011111111", "email@email.com", address);
        assertThat(customer.getAddress().getCity()).isEqualTo("city");
        assertThat(customer.getAddress().getStreet()).isEqualTo("street");
        assertThat(customer.getAddress().getZipcode()).isEqualTo("88888");
    }

    /**
     * 포인트는 음수일 수 없습니다.
     */
    @Test
    void point() {
        Address address = new Address("city", "street", "88888");

        assertThatThrownBy(() -> new Customer("loginid", "이름", "Password1!",
                "01011111111", "email@email.com", address, -100))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(CustomerPolicy.POINT_POLICY_STRING);

        Customer customer = new Customer("loginid", "이름", "Password1!",
                "01011111111", "email@email.com", address, 100);

        assertThat(customer.getPoint()).isEqualTo(100);
    }

    @Test
    void isDeleted() {
        Address address = new Address("city", "street", "88888");
        Customer customer = new Customer("loginid", "이름", "Password1!",
                "01011111111", "email@email.com", address, 100, false);

        assertThat(customer.isDeleted()).isFalse();
    }
}