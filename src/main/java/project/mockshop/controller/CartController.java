package project.mockshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import project.mockshop.dto.*;
import project.mockshop.response.Response;
import project.mockshop.service.CartService;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping("/cart")
    public Response addToCart(@RequestBody CartAddRequestDto cartRequest) {
        Long cartId = cartService.addToCart(cartRequest.getItemId(), cartRequest.getCount(), cartRequest.getCustomerId());
        return Response.success(cartId);
    }

    @GetMapping("/cart")
    public Response getCartWithItems(@RequestParam Long customerId) {
        CartDto cartDto = cartService.getCartWithItems(customerId);
        return Response.success(cartDto);
    }

    @PutMapping("/cart")
    public Response changeCartItemCount(@RequestBody CartChangeRequestDto changeRequestDto) {
        cartService.changeCartItemCount(changeRequestDto.getCartId(),
                changeRequestDto.getCartItemId(),
                changeRequestDto.getCount());
        return Response.success();
    }

    @DeleteMapping("/cart")
    public Response removeCartItem(@RequestBody CartDeleteRequestDto deleteRequestDto) {
        cartService.removeCartItem(deleteRequestDto.getCartId(), deleteRequestDto.getCartItemId());
        return Response.success();
    }
}
