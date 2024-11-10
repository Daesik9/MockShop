package project.mockshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.mockshop.dto.CouponDto;
import project.mockshop.dto.CouponItemDto;
import project.mockshop.entity.Coupon;
import project.mockshop.entity.CouponItem;
import project.mockshop.entity.Customer;
import project.mockshop.mapper.CouponItemMapper;
import project.mockshop.mapper.CouponMapper;
import project.mockshop.policy.MockShopPolicy;
import project.mockshop.repository.CouponItemRepository;
import project.mockshop.repository.CouponRepository;
import project.mockshop.repository.CustomerRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponService {
    private final CouponRepository couponRepository;
    private final CouponItemRepository couponItemRepository;
    private final CustomerRepository customerRepository;

    @Transactional
    public Long createCoupon(CouponDto couponDto) {
        if (couponDto.getMinPriceRequired() < couponDto.getPriceOff()) {
            throw new IllegalStateException(MockShopPolicy.INPUT_STRING_METHOD("사용조건"));
        }

        Coupon coupon = CouponMapper.toEntity(couponDto);
        couponRepository.save(coupon);
        return coupon.getId();
    }

    @Transactional
    public Long issueCoupon(Long couponId, Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new NoSuchElementException("해당 구매자가 없습니다."));
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new NoSuchElementException("해당 쿠폰이 없습니다."));

        CouponItem couponItem = CouponItem.builder().coupon(coupon).customer(customer).isUsed(false).build();
        couponItemRepository.save(couponItem);

        return couponItem.getId();
    }

    public List<CouponItemDto> getAllCouponItemsByCustomerId(Long customerId) {
        return couponItemRepository.findAllByCustomerId(customerId).stream().map(CouponItemMapper::toDto).toList();
    }

    public List<CouponItemDto> getAvailableCoupons(Long customerId, Integer totalPrice) {
        return couponItemRepository.findAvailableCouponsByCustomerIdAndTotalPrice(customerId, totalPrice).stream().map(CouponItemMapper::toDto).toList();
    }

    public int useCoupon(Long couponItemId, int totalPrice) {
        CouponItem couponItem = couponItemRepository.findById(couponItemId)
                .orElseThrow();
        Coupon coupon = couponItem.getCoupon();

        couponItem.changeIsUsed(true); //쿠폰 사용처리

        if (coupon.getPercentOff() > 0) {
            int discountAmount = (int) (totalPrice * coupon.getPercentOff() / 100);

            return Math.min(discountAmount, coupon.getMaxPriceOff());
        } else {
            return coupon.getPriceOff();
        }
    }

}
