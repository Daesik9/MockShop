package project.mockshop.mapper;

import project.mockshop.dto.CouponDto;
import project.mockshop.entity.Coupon;

public class CouponMapper {
    public static Coupon toEntity(CouponDto couponDto) {
        return Coupon.builder()
                .id(couponDto.getId())
                .name(couponDto.getName())
                .priceOff(couponDto.getPriceOff())
                .percentOff(couponDto.getPercentOff())
                .expiredDate(couponDto.getExpiredDate())
                .minPriceRequired(couponDto.getMinPriceRequired())
                .maxPriceOff(couponDto.getMaxPriceOff())
                .build();
    }

    public static CouponDto toDto(Coupon coupon) {
        return CouponDto.builder()
                .id(coupon.getId())
                .name(coupon.getName())
                .priceOff(coupon.getPriceOff())
                .percentOff(coupon.getPercentOff())
                .expiredDate(coupon.getExpiredDate())
                .minPriceRequired(coupon.getMinPriceRequired())
                .maxPriceOff(coupon.getMaxPriceOff())
                .build();
    }
}