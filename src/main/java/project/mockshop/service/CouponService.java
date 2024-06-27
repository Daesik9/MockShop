package project.mockshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.mockshop.dto.CouponDto;
import project.mockshop.entity.Coupon;
import project.mockshop.mapper.CouponMapper;
import project.mockshop.policy.MockShopPolicy;
import project.mockshop.repository.CouponRepository;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;

    public Long createCoupon(CouponDto couponDto) {
        if (couponDto.getMinPriceRequired() < couponDto.getPriceOff()) {
            throw new IllegalStateException(MockShopPolicy.INPUT_STRING_METHOD("사용조건"));
        }

        Coupon coupon = CouponMapper.toEntity(couponDto);
        couponRepository.save(coupon);
        return coupon.getId();
    }
}
