package project.mockshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import project.mockshop.dto.CustomerCreationDto;
import project.mockshop.dto.CustomerDto;
import project.mockshop.dto.LoginRequestDto;
import project.mockshop.response.Response;
import project.mockshop.service.CustomerService;

import java.util.List;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping("/users")
    public Response createAccount(@RequestBody CustomerCreationDto creationDto) {
        customerService.createAccount(creationDto);

        return Response.success(HttpStatus.CREATED.value());
    }

    @GetMapping("/users")
    public Response findAll() {
        List<CustomerDto> customers = customerService.findAll();

        return Response.success(customers);
    }

    @GetMapping("/users/{id}")
    public Response findOne(@PathVariable Long id) {
        return Response.success(customerService.findOne(id));
    }

    @GetMapping("/users/find/login-id")
    public Response findLoginId(@RequestParam String phoneNumber) {
        CustomerDto customer = customerService.findLoginId(phoneNumber);
        return Response.success(customer);
    }

    @GetMapping("/users/find/password")
    public Response findPassword(@RequestParam String loginId,
                                                    @RequestParam String phoneNumber) {
        CustomerDto customer = customerService.findPassword(loginId, phoneNumber);
        return Response.success(customer);
    }

    @PostMapping("/users/login")
    public Response login(@RequestBody LoginRequestDto loginRequestDto) {
        CustomerDto customer = customerService.login(loginRequestDto);

        return Response.success(customer);
    }

    @GetMapping("/users/check-duplicate/{loginId}")
    public Response checkDuplicate(@PathVariable String loginId) {
        customerService.validateDuplicateLoginId(loginId);

        return Response.success(false);
    }
}
