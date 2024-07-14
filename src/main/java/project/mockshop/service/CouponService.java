package project.mockshop.service;

import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
public class CouponService {
    private final CouponRepository couponRepository;
    private final CouponItemRepository couponItemRepository;
    private final CustomerRepository customerRepository;

    public Long createCoupon(CouponDto couponDto) {
        if (couponDto.getMinPriceRequired() < couponDto.getPriceOff()) {
            throw new IllegalStateException(MockShopPolicy.INPUT_STRING_METHOD("사용조건"));
        }

        Coupon coupon = CouponMapper.toEntity(couponDto);
        couponRepository.save(coupon);
        return coupon.getId();
    }

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

    //쿠폰 발급 선착순 몇명 -(이벤트에 따라서 쿠폰 개수랑, 항목, 선착순이 다름)
    //eventId, customerId -> 이벤트 아이디로 해당 이벤트를 찾아서 쿠폰 항목, 개수, 선착순을 보고, 이미 참여했는지도 보고
    //cusotomerId로 찾은 구매자에게 쿠폰을 발급
//    coupon_item	id
//    coupon_id
//            customer_id
//    use_yn

}
