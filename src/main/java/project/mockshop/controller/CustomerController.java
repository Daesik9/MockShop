package project.mockshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import project.mockshop.dto.*;
import project.mockshop.entity.CouponItem;
import project.mockshop.response.Response;
import project.mockshop.service.CouponService;
import project.mockshop.service.CustomerService;

import java.util.List;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    private final CouponService couponService;

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
    public Response findLoginIdByEmail(@RequestBody FindLoginIdRequestDto requestDto) {
        String loginId = customerService.findLoginIdByEmail(requestDto.getEmail());
        return Response.success(loginId);
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

    @PutMapping("/users")
    public Response updateUserProfile(@RequestBody UpdateProfileDto updateProfileDto) {
        customerService.updateProfile(updateProfileDto);

        return Response.success();
    }

    @GetMapping("/users/{customerId}/coupons")
    public Response getAllCoupons(@PathVariable Long customerId) {
        List<CouponItemDto> couponItemDtos = couponService.getAllCouponItemsByCustomerId(customerId);

        return Response.success(couponItemDtos);
    }

    @GetMapping("/users/{customerId}/coupons/available-for-order")
    public Response getAvailableCoupons(@PathVariable Long customerId , @RequestParam Integer orderAmount) {
        List<CouponItemDto> availableCoupons = couponService.getAvailableCoupons(customerId, orderAmount);

        return Response.success(availableCoupons);
    }

    @PutMapping("/users/reset-password")
    public Response resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) {
        customerService.resetPassword(resetPasswordDto.getEmail(), resetPasswordDto.getPassword());

        return Response.success();
    }
}
