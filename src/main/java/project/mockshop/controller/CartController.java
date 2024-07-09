package project.mockshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import project.mockshop.dto.CartAddRequestDto;
import project.mockshop.dto.CartItemDto;
import project.mockshop.response.Response;
import project.mockshop.service.CartService;

import java.util.List;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping("/cart")
    public Response addToCart(@RequestBody CartAddRequestDto cartRequest) {
        cartService.addToCart(cartRequest.getItemId(), cartRequest.getCount(), 1L);
        return Response.success(true);
    }

    @GetMapping("/cart")
    public Response getCartItems(@RequestParam Long customerId) {
        List<CartItemDto> cartItems = cartService.getCartItems(customerId);
        return Response.success(cartItems);
    }

}
