package project.mockshop.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OrderRequestDto {

    private Long customerId;
    private String paymentMethod;
}
