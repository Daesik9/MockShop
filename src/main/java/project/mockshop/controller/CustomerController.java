package project.mockshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import project.mockshop.dto.*;
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

    @PostMapping("/users/find/login-id")
    public Response findLoginId(@RequestBody FindLoginIdRequestDto requestDto) {
        CustomerDto customer = customerService.findLoginId(requestDto.getPhoneNumber());
        return Response.success(customer.getLoginId());
    }

    @PostMapping("/users/find/password")
    public Response findPassword(@RequestBody FindPasswordRequestDto requestDto) {
        CustomerDto customer = customerService.findPassword(requestDto.getLoginId(), requestDto.getPhoneNumber());
        return Response.success(customer.getPassword());
    }

    @PostMapping("/users/login")
    public Response login(@RequestBody LoginRequestDto loginRequestDto) {
        CustomerDto customer = customerService.login(loginRequestDto);

        return Response.success(customer);
    }

    @GetMapping("/users/check-duplicate/{loginId}")
    public Response checkDuplicate(@PathVariable String loginId) {
        boolean isDuplicated = customerService.validateDuplicateLoginId(loginId);

        return Response.success(isDuplicated);
    }

//    @PutMapping("/users")
//    public Response updateUserProfile(@RequestBody UpdateProfileDto updateProfileDto) {
//        customerService.updateProfile(updateProfileDto);
//
//        return Response.success();
//    }
}
