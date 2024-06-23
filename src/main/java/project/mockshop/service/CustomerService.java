package project.mockshop.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.mockshop.dto.CustomerCreationDto;
import project.mockshop.dto.CustomerDto;
import project.mockshop.dto.LoginRequestDto;
import project.mockshop.entity.Customer;
import project.mockshop.mapper.CustomerMapper;
import project.mockshop.policy.CustomerPolicy;
import project.mockshop.repository.CustomerRepository;
import project.mockshop.validator.CustomerValidator;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public Long createAccount(CustomerCreationDto dto) {
        validateDuplicateLoginId(dto.getLoginId());
        CustomerValidator.validateLoginId(dto.getLoginId());
        CustomerValidator.validateName(dto.getName());
        CustomerValidator.validatePassword(dto.getPassword());
        CustomerValidator.validatePhoneNumber(dto.getPhoneNumber());
        CustomerValidator.validateEmail(dto.getEmail());

        Customer newCustomer = customerRepository.save(CustomerMapper.toEntity(dto));

        return newCustomer.getId();
    }

    public void validateDuplicateLoginId(String loginId) {
        Optional<Customer> findCustomer = customerRepository.findByLoginId(loginId);
        if (findCustomer.isPresent()) {
            throw new IllegalStateException(CustomerPolicy.DUPLICATE_LOGIN_ID_STRING);
        }
    }

    public CustomerDto login(LoginRequestDto loginRequest) {
        Optional<Customer> findCustomer = customerRepository.findByLoginId(loginRequest.getLoginId());

        //아이디가 존재하지 않을 때
        if (findCustomer.isEmpty()) {
            throw new NullPointerException("아이디나 비밀번호가 일치하지 않습니다.");
        }

        //비밀번호가 일치하지 않을 때
        if (!findCustomer.get().getPassword().equals(loginRequest.getPassword())) {
            throw new NullPointerException("아이디나 비밀번호가 일치하지 않습니다.");
        }

        return CustomerMapper.toDto(findCustomer.get());
    }

    public CustomerDto findLoginId(String phoneNumber) {
        Optional<Customer> findCustomer = customerRepository.findLoginIdByPhoneNumber(phoneNumber);

        if (findCustomer.isEmpty()) {
            throw new NullPointerException("입력한 핸드폰 번호와 일치하는 아이디가 없습니다.");
        }

        return CustomerMapper.toDto(findCustomer.get());
    }

    public CustomerDto findPassword(String loginId, String phoneNumber) {
        Optional<Customer> findCustomer = customerRepository.findByLoginId(loginId);

        //로그인 아이디와 일치하는 정보가 없을 때
        if (findCustomer.isEmpty()) {
            throw new NullPointerException("해당 로그인 아이디와 일치하는 정보가 없습니다.");
        }

        //로그인 아이디와 핸드폰 번호가 일치하지 않을 때
        if (!findCustomer.get().getPhoneNumber().equals(phoneNumber)) {
            throw new NullPointerException("해당 로그인 아이디와 입력하신 핸드폰 번호가 일치하지 않습니다.");
        }

        return CustomerMapper.toDto(findCustomer.get());
    }

    public CustomerDto findOne(Long id) {
        Optional<Customer> findCustomer = customerRepository.findById(id);
        return findCustomer.map(CustomerMapper::toDto).orElse(null);
    }

    public void resetPassword(CustomerDto customer, String password) {
        Optional<Customer> findCustomer = customerRepository.findById(customer.getId());
        findCustomer.ifPresent(value -> value.changePassword(password));
    }

    public List<CustomerDto> findAll() {
        return customerRepository.findAll()
                .stream().map(CustomerMapper::toDto)
                .toList();
    }
}
