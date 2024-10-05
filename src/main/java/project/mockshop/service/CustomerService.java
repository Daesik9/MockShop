package project.mockshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.mockshop.dto.CustomerCreationDto;
import project.mockshop.dto.CustomerDto;
import project.mockshop.dto.LoginRequestDto;
import project.mockshop.dto.UpdateProfileDto;
import project.mockshop.entity.Customer;
import project.mockshop.entity.Role;
import project.mockshop.mapper.CustomerMapper;
import project.mockshop.policy.CustomerPolicy;
import project.mockshop.repository.CustomerRepository;
import project.mockshop.validator.CustomerValidator;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public Long createAccount(CustomerCreationDto dto) {
        if (validateDuplicateLoginId(dto.getLoginId())) {
            throw new IllegalStateException(CustomerPolicy.DUPLICATE_LOGIN_ID_STRING);
        }
        CustomerValidator.validateLoginId(dto.getLoginId());
        CustomerValidator.validateName(dto.getName());
        CustomerValidator.validatePassword(dto.getPassword());
        CustomerValidator.validatePhoneNumber(dto.getPhoneNumber());
        CustomerValidator.validateEmail(dto.getEmail());

        Customer customer = CustomerMapper.toEntity(dto);
        customer.changeRole(Role.CUSTOMER.name());
        customer.changePassword(bCryptPasswordEncoder.encode(customer.getPassword()));

        Customer newCustomer = customerRepository.save(customer);

        return newCustomer.getId();
    }

    public boolean validateDuplicateLoginId(String loginId) {
        Optional<Customer> findCustomer = customerRepository.findByLoginId(loginId);
        return findCustomer.isPresent();
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

    @Transactional
    public void resetPassword(CustomerDto customer, String password) {
        Optional<Customer> findCustomer = customerRepository.findById(customer.getId());
        findCustomer.ifPresent(value -> value.changePassword(password));
    }

    public List<CustomerDto> findAll() {
        return customerRepository.findAll()
                .stream().map(CustomerMapper::toDto)
                .toList();
    }

    @Transactional
    public void updateProfile(UpdateProfileDto updateProfileDto) {
        Optional<Customer> optionalCustomer = customerRepository.findById(updateProfileDto.getUserId());
        if (optionalCustomer.isEmpty()) {
            throw new NullPointerException("해당 유저가 없습니다.");
        }

        Customer customer = optionalCustomer.get();

        if (customer.getPassword().equals(updateProfileDto.getPassword())) {
            throw new IllegalArgumentException(CustomerPolicy.SAME_PASSWORD_STRING);
        }

        customer.changeName(updateProfileDto.getName());
        if (!updateProfileDto.getPassword().isEmpty()) { // 비밀번호를 변경 안 하는 경우에는 그냥 빈 칸으로 보냄.
            customer.changePassword(updateProfileDto.getPassword());
        }
        customer.changeEmail(updateProfileDto.getEmail());
        customer.changePhoneNumber(updateProfileDto.getPhoneNumber());
        customer.changeAddress(updateProfileDto.getAddress());
    }
}