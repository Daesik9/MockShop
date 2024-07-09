package project.mockshop.mapper;

import project.mockshop.dto.CartItemDto;
import project.mockshop.entity.CartItem;

public class CartItemMapper {
    public static CartItemDto toDto(CartItem cartItem) {
        return CartItemDto.builder()
                .id(cartItem.getId())
                .itemDto(ItemMapper.toDto(cartItem.getItem()))
                .cartPrice(cartItem.getCartPrice())
                .count(cartItem.getCount())
                .build();
    }
}
