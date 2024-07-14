package project.mockshop.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CouponItemDto {
    private Long id;
    private CustomerDto customerDto;
    private CouponDto couponDto;
    private boolean isUsed;
}
