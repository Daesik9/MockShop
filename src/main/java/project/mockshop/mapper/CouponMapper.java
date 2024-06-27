package project.mockshop.mapper;

import project.mockshop.dto.CouponDto;
import project.mockshop.entity.Coupon;

public class CouponMapper {
    public static Coupon toEntity(CouponDto couponDto) {
        return Coupon.builder()
                .name(couponDto.getName())
                .priceOff(couponDto.getPriceOff())
                .percentOff(couponDto.getPercentOff())
                .expiredDate(couponDto.getExpiredDate())
                .minPriceRequired(couponDto.getMinPriceRequired())
                .maxPriceOff(couponDto.getMaxPriceOff())
                .build();
    }
}