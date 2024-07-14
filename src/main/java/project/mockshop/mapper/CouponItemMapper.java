package project.mockshop.mapper;

import project.mockshop.dto.CouponItemDto;
import project.mockshop.entity.CouponItem;

public class CouponItemMapper {
    public static CouponItemDto toDto(CouponItem couponItem) {
        return CouponItemDto.builder()
                .id(couponItem.getId())
                .customerDto(CustomerMapper.toDto(couponItem.getCustomer()))
                .couponDto(CouponMapper.toDto(couponItem.getCoupon()))
                .isUsed(couponItem.isUsed())
                .build();
    }
}
