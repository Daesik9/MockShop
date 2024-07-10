package project.mockshop.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CartChangeRequestDto {
    private Long cartId;
    private Long cartItemId;
    private int count;
}
