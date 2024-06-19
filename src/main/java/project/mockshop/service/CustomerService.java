package project.mockshop.service;

import project.mockshop.dto.LoginRequestDto;
import project.mockshop.entity.Customer;
import project.mockshop.policy.CustomerPolicy;
import project.mockshop.repository.CustomerRepository;

import java.util.Optional;

public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void createAccount(Customer customer) {
        validateDuplicateLoginId(customer);
        customerRepository.save(customer);
    }

    private void validateDuplicateLoginId(Customer customer) {
        Optional<Customer> findCustomer = customerRepository.findByLoginId(customer.getLoginId());
        if (findCustomer.isPresent()) {
            throw new IllegalStateException(CustomerPolicy.DUPLICATE_LOGIN_ID_STRING);
        }
    }

    public Customer login(LoginRequestDto loginRequest) {
        Optional<Customer> findCustomer = customerRepository.findByLoginId(loginRequest.getLoginId());

        //아이디가 존재하지 않을 때
        if (findCustomer.isEmpty()) {
            throw new NullPointerException("아이디나 비밀번호가 일치하지 않습니다.");
        }

        //비밀번호가 일치하지 않을 때
        if (!findCustomer.get().getPassword().equals(loginRequest.getPassword())) {
            throw new NullPointerException("아이디나 비밀번호가 일치하지 않습니다.");
        }

        return findCustomer.get();
    }

    public Customer findLoginId(String phoneNumber) {
        Optional<Customer> findCustomer = customerRepository.findLoginIdByPhoneNumber(phoneNumber);

        if (findCustomer.isEmpty()) {
            throw new NullPointerException("입력한 핸드폰 번호와 일치하는 아이디가 없습니다.");
        }

        return findCustomer.get();
    }

    public Customer findPassword(String loginId, String phoneNumber) {
        Optional<Customer> findCustomer = customerRepository.findByLoginId(loginId);

        //로그인 아이디와 일치하는 정보가 없을 때
        if (findCustomer.isEmpty()) {
            throw new NullPointerException("해당 로그인 아이디와 일치하는 정보가 없습니다.");
        }

        //로그인 아이디와 핸드폰 번호가 일치하지 않을 때
        if (!findCustomer.get().getPhoneNumber().equals(phoneNumber)) {
            throw new NullPointerException("해당 로그인 아이디와 입력하신 핸드폰 번호가 일치하지 않습니다.");
        }

        return findCustomer.get();
    }

    public Customer findOne(Long id) {
        Optional<Customer> findCustomer = customerRepository.findById(id);
        return findCustomer.orElse(null);
    }

    public void resetPassword(Customer customer, String password) {
        customer.changePassword(password);
    }
}
