package project.mockshop.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartRequestDto {
    private Long itemId;
    private int count;
}
