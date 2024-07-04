package project.mockshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.mockshop.dto.CartRequestDto;
import project.mockshop.response.Response;
import project.mockshop.service.CartService;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping("/cart")
    public Response addToCart(@RequestBody CartRequestDto cartRequest) {
        cartService.addToCart(cartRequest.getItemId(), cartRequest.getCount(), 1L);
        return Response.success(true);
    }

}
