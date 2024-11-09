package project.mockshop.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import project.mockshop.entity.AddressInfo;
import project.mockshop.entity.Customer;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class CustomerRepositoryTest {

    @Autowired
    CustomerRepository customerRepository;

    @Test
    void repositoryIsNotNull() {
        assertThat(customerRepository).isNotNull();
    }

    @Test
    void create() {
        //given
        Customer customer = Customer.builder().loginId("customer").build();

        //when
        customerRepository.save(customer);
        Customer findCustomer = customerRepository.findById(customer.getId()).get();

        //then
        assertThat(findCustomer.getLoginId()).isEqualTo(customer.getLoginId());
    }

    @Test
    void listRead() {
        //given
        Customer customer1 = Customer.builder().loginId("customer1").build();
        Customer customer2 = Customer.builder().loginId("customer2").build();

        customerRepository.save(customer1);
        customerRepository.save(customer2);

        //when
        List<Customer> customers = customerRepository.findAll();

        //then
        assertThat(customers.size()).isEqualTo(2);
        assertThat(customers.get(0)).isEqualTo(customer1);
        assertThat(customers.get(1)).isEqualTo(customer2);
    }

    /**
     * 비밀번호, 이름, 핸드폰번호, 이메일주소, 주소만 변경 가능하다.
     */
    @Test
    void update() {
        //given
        AddressInfo addressInfo = new AddressInfo("city", "street", "12345");
        Customer customer = Customer.builder()
                .loginId("customer")
                .name("이름")
                .password("Password1!")
                .phoneNumber("01011111111")
                .email("email@email.com")
                .addressInfo(addressInfo)
                .build();
        customerRepository.save(customer);

        //when
        Customer findCustomer = customerRepository.findById(customer.getId()).orElse(null);
        if (findCustomer == null) {
            throw new NullPointerException();
        }

        findCustomer.changePassword("Newpassword1!");
        findCustomer.changeName("새이름");
        findCustomer.changePhoneNumber("01012341234");
        findCustomer.changeEmail("newemail@email.com");
        AddressInfo newAddressInfo = new AddressInfo("new city", "new street", "22222");
        findCustomer.changeAddressInfo(newAddressInfo);

        //then
        Customer updatedCustomer = customerRepository.findById(customer.getId()).orElse(null);
        if (updatedCustomer == null) {
            throw new NullPointerException();
        }

        assertThat(updatedCustomer.getPassword()).isEqualTo("Newpassword1!");
        assertThat(updatedCustomer.getName()).isEqualTo("새이름");
        assertThat(updatedCustomer.getPhoneNumber()).isEqualTo("01012341234");
        assertThat(updatedCustomer.getEmail()).isEqualTo("newemail@email.com");
        assertThat(updatedCustomer.getAddressInfo()).isEqualTo(newAddressInfo);
    }

    @Test
    void delete() {
        //given
        Customer customer1 = Customer.builder().loginId("customer1").build();
        Customer customer2 = Customer.builder().loginId("customer2").build();

        customerRepository.save(customer1);
        customerRepository.save(customer2);

        //when
        customerRepository.delete(customer1);

        //then
        List<Customer> customers = customerRepository.findAll();

        assertThat(customers.size()).isEqualTo(1);
        assertThat(customerRepository.findById(customer1.getId())).isEmpty();
    }

    @Test
    void findByEmail() throws Exception {
        //given
        Customer customer = Customer.builder().loginId("customer").email("email@email.com").build();
        customerRepository.save(customer);

        //when
        Optional<Customer> byEmail = customerRepository.findByEmail("email@email.com");

        //then
        assertThat(byEmail).isNotEmpty();
        assertThat(byEmail.get().getEmail()).isEqualTo("email@email.com");
    }


}