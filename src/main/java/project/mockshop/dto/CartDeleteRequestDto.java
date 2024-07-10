package project.mockshop.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CartDeleteRequestDto {
    private Long cartId;
    private Long cartItemId;
}
