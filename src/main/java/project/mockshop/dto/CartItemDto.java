package project.mockshop.dto;

import lombok.Builder;
import lombok.Getter;
import project.mockshop.entity.Item;

@Builder
@Getter
public class CartItemDto {
    private Long id;
    private Item item;
    private int cartPrice;
    private int count;
}
