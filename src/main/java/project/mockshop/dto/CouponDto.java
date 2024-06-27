package project.mockshop.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class CouponDto {
    private String name;
    private int priceOff;
    private double percentOff;
    private LocalDateTime expiredDate;
    private int minPriceRequired;
    private int maxPriceOff;

}