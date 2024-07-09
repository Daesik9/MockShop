package project.mockshop.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CartItemDto {
    private Long id;
    private ItemDto itemDto;
    private int cartPrice;
    private int count;
}
