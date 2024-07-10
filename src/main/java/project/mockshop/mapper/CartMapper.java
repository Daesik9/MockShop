package project.mockshop.mapper;

import project.mockshop.dto.CartDto;
import project.mockshop.entity.Cart;

public class CartMapper {


    public static CartDto toDto(Cart cart) {
        return CartDto.builder()
                .id(cart.getId())
                .cartItemDtos(cart.getCartItems().stream().map(CartItemMapper::toDto).toList())
                .customerDto(CustomerMapper.toDto(cart.getCustomer()))
                .build();
    }
}
