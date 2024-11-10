package project.mockshop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.mockshop.dto.ItemDto;
import project.mockshop.dto.OrderDto;
import project.mockshop.dto.OrderRequestDto;
import project.mockshop.response.Response;
import project.mockshop.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/orders")
    public Response order(@RequestBody OrderRequestDto requestDto) {
        log.info("/api/orders, requestBody={}", requestDto);

        String orderNumber = orderService.order(requestDto.getCustomerId(),
                requestDto.getPaymentMethod(),
                requestDto.getCouponItemId());

        return Response.success(orderNumber);
    }

    @GetMapping("/orders/customers/{customerId}")
    public Response getOrderHistory(@PathVariable Long customerId) {

        List<OrderDto> orderDtos = orderService.findAllByCustomerId(customerId);

        return Response.success(orderDtos);
    }

    @GetMapping("/orders/{orderNumber}")
    public Response getOrderDetail(@PathVariable String orderNumber) {

        OrderDto orderDto = orderService.findByOrderNumber(orderNumber);

        return Response.success(orderDto);
    }

    @GetMapping("/orders/merchants/{merchantId}")
    public Response getOrdersByMerchant(@PathVariable Long merchantId) {
        List<OrderDto> ordersByMerchant = orderService.findAllByMerchantId(merchantId);
        return Response.success(ordersByMerchant);
    }


}