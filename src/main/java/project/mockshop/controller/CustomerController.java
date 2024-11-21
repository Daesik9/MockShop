package project.mockshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import project.mockshop.dto.*;
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

    @PostMapping("/members")
    public Response createAccount(@RequestBody CustomerCreationDto creationDto) {
        customerService.createAccount(creationDto);

        return Response.success(HttpStatus.CREATED.value());
    }

    @GetMapping("/members")
    public Response findAll() {
        List<CustomerDto> customers = customerService.findAll();

        return Response.success(customers);
    }

    @GetMapping("/members/{id}")
    public Response findOne(@PathVariable Long id) {
        return Response.success(customerService.findOne(id));
    }

    @PostMapping("/members/find-login-id")
    public Response findLoginIdByEmail(@RequestBody FindLoginIdRequestDto requestDto) {
        String loginId = customerService.findLoginIdByEmail(requestDto.getEmail());
        return Response.success(loginId);
    }

    @GetMapping("/members/check-duplicate/{loginId}")
    public Response checkDuplicate(@PathVariable String loginId) {
        boolean isDuplicated = customerService.validateDuplicateLoginId(loginId);

        return Response.success(isDuplicated);
    }

    @PutMapping("/members")
    public Response updateUserProfile(@RequestBody UpdateProfileDto updateProfileDto) {
        customerService.updateProfile(updateProfileDto);

        return Response.success();
    }

    @GetMapping("/members/{customerId}/coupons")
    public Response getAllCoupons(@PathVariable Long customerId) {
        List<CouponItemDto> couponItemDtos = couponService.getAllCouponItemsByCustomerId(customerId);

        return Response.success(couponItemDtos);
    }

    @GetMapping("/members/{customerId}/coupons/available-for-order")
    public Response getAvailableCoupons(@PathVariable Long customerId , @RequestParam Integer orderAmount) {
        List<CouponItemDto> availableCoupons = couponService.getAvailableCoupons(customerId, orderAmount);

        return Response.success(availableCoupons);
    }

    @PutMapping("/members/reset-password")
    public Response resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) {
        customerService.resetPassword(resetPasswordDto.getEmail(), resetPasswordDto.getPassword());

        return Response.success();
    }
}
