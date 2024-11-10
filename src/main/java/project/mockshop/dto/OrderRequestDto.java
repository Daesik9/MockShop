package project.mockshop.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class OrderRequestDto {

    private Long customerId;
    private String paymentMethod;
    private int pointUsed;
    private Long couponItemId;
}
