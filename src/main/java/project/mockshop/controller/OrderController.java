package project.mockshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.mockshop.dto.OrderRequestDto;
import project.mockshop.response.Response;
import project.mockshop.service.OrderService;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/orders")
    public Response order(@RequestBody OrderRequestDto requestDto) {

        Long orderId = orderService.order(requestDto.getCustomerId(), requestDto.getPaymentMethod());

        return Response.success(orderId);
    }
}