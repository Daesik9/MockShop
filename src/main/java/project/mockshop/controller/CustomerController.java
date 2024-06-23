package project.mockshop.controller;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.mockshop.dto.CustomerCreationDto;
import project.mockshop.dto.CustomerDto;
import project.mockshop.dto.LoginRequestDto;
import project.mockshop.entity.Address;
import project.mockshop.entity.Customer;
import project.mockshop.mapper.CustomerMapper;
import project.mockshop.service.CustomerService;

import java.util.List;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
@Slf4j
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping("/users")
    public ResponseEntity createAccount(@RequestBody CustomerCreationDto creationDto) {

        try {
            customerService.createAccount(creationDto);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }


        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/users")
    public ResponseEntity<List<CustomerDto>> findAll() {
        List<CustomerDto> customers = customerService.findAll();

        return ResponseEntity.status(HttpStatus.OK).body(customers);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<CustomerDto> findOne(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(customerService.findOne(id));
    }

    @GetMapping("/users/find/login-id")
    public ResponseEntity<CustomerDto> findLoginId(@RequestParam String phoneNumber) {
        return ResponseEntity.status(HttpStatus.OK).body(customerService.findLoginId(phoneNumber));
    }

    @GetMapping("/users/find/password")
    public ResponseEntity<CustomerDto> findPassword(@RequestParam String loginId,
                                                    @RequestParam String phoneNumber) {
        return ResponseEntity.status(HttpStatus.OK).body(customerService.findPassword(loginId, phoneNumber));
    }

    @PostMapping("/users/login")
    public ResponseEntity login(@RequestBody LoginRequestDto loginRequestDto) {
        CustomerDto customer = customerService.login(loginRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(customer);
    }

    @GetMapping("/users/check-duplicate/{loginId}")
    public ResponseEntity checkDuplicate(@PathVariable String loginId) {
        try {
            customerService.validateDuplicateLoginId(loginId);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
