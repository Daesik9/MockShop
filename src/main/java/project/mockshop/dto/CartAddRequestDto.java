package project.mockshop.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartAddRequestDto {
    private Long itemId;
    private int count;
    private Long customerId;
}
