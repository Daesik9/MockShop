package project.mockshop.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.mockshop.dto.CouponDto;
import project.mockshop.dto.CouponItemDto;
import project.mockshop.entity.Coupon;
import project.mockshop.entity.CouponItem;
import project.mockshop.entity.Customer;
import project.mockshop.policy.MockShopPolicy;
import project.mockshop.repository.CouponItemRepository;
import project.mockshop.repository.CouponRepository;
import project.mockshop.repository.CustomerRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@Transactional
public class CouponServiceSpringTest {
    @Autowired
    private CouponService couponService;
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CouponItemRepository couponItemRepository;


    @Test
    void notNull() throws Exception {
        //given

        //when

        //then
        assertThat(couponService).isNotNull();
    }


    @Test
    void createCoupon() throws Exception {
        //given
        CouponDto couponDtoPriceOff = CouponDto.builder()
                .name("coupon")
                .priceOff(1000)
                .expiredDate(LocalDateTime.now().plusDays(30))
                .minPriceRequired(1000)
                .build();

        //when
        Long couponId = couponService.createCoupon(couponDtoPriceOff);

        //then
        Coupon findCoupon = couponRepository.findById(couponId).orElse(null);
        assertThat(findCoupon).isNotNull();
        assertThat(findCoupon.getName()).isEqualTo("coupon");
    }

    @Test
    void createCoupon_fail() throws Exception {
        //given
        // 사용조건이 할인 가격보다 적은 경우 -> 가격이 마이너스가 나올 수 있으니 오류 발생
        CouponDto couponDtoPriceOff = CouponDto.builder()
                .name("coupon")
                .priceOff(1000)
                .expiredDate(LocalDateTime.now().plusDays(30))
                .minPriceRequired(900)
                .build();

        // 할인 가격과 할인율이 중복으로 적용된 경우 -> 오류 발생
        CouponDto couponDtoBoth = CouponDto.builder()
                .name("coupon")
                .priceOff(1000)
                .percentOff(10)
                .minPriceRequired(1000)
                .expiredDate(LocalDateTime.now().plusDays(30))
                .build();

        //when
        assertThatThrownBy(() -> couponService.createCoupon(couponDtoPriceOff))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(MockShopPolicy.INPUT_STRING_METHOD("사용조건"));

        assertThatThrownBy(() -> couponService.createCoupon(couponDtoBoth))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("할인율/가격 둘 중 하나만 입력 가능합니다.");

        //then
    }

    @Test
    void getAllCouponItemsByCustomerId() throws Exception {
        //given
        Coupon coupon = Coupon.builder().build();
        Customer customer = Customer.builder().build();
        CouponItem couponItem = CouponItem.builder().customer(customer).coupon(coupon).build();
        couponRepository.save(coupon);
        customerRepository.save(customer);
        couponItemRepository.save(couponItem);

        //when
        List<CouponItemDto> couponItemDtos = couponService.getAllCouponItemsByCustomerId(customer.getId());

        //then
        assertThat(couponItemDtos.size()).isEqualTo(1);
    }


    @Test
    void issueCoupon() throws Exception {
        //given
        Coupon coupon = Coupon.builder().build();
        Customer customer = Customer.builder().build();
        CouponItem couponItem = CouponItem.builder().coupon(coupon).customer(customer).build();
        couponRepository.save(coupon);
        customerRepository.save(customer);

        //when
        Long couponItemId = couponService.issueCoupon(coupon.getId(), customer.getId());
        List<CouponItemDto> couponItemDtos = couponService.getAllCouponItemsByCustomerId(customer.getId());

        //then
        assertThat(couponItemDtos.size()).isEqualTo(1);
    }

    @Test
    void useCoupon_priceOff() throws Exception {
        //given
        Customer customer = Customer.builder().build();
        customerRepository.save(customer);

        Coupon coupon = Coupon.builder().priceOff(3000).build();
        couponRepository.save(coupon);

        CouponItem couponItem = CouponItem.builder().coupon(coupon).customer(customer).build();
        couponItemRepository.save(couponItem);

        //when
        int discountAmount = couponService.useCoupon(couponItem.getId(), 10000);

        //then
        assertThat(discountAmount).isEqualTo(3000);
    }

    @Test
    void useCoupon_percentOff() throws Exception {
        //given
        Customer customer = Customer.builder().build();
        customerRepository.save(customer);

        Coupon coupon_max2000 = Coupon.builder().percentOff(30).maxPriceOff(2000).build();
        Coupon coupon_max5000 = Coupon.builder()
                .name("coupon_max5000")
                .percentOff(23)
                .maxPriceOff(5000)
                .expiredDate(LocalDateTime.now().plusDays(30))
                .build();
        couponRepository.save(coupon_max2000);
        couponRepository.save(coupon_max5000);

        CouponItem couponItem = CouponItem.builder().coupon(coupon_max2000).customer(customer).build();
        CouponItem couponItem2 = CouponItem.builder().coupon(coupon_max5000).customer(customer).build();
        couponItemRepository.save(couponItem);
        couponItemRepository.save(couponItem2);

        //when
        int discountAmount = couponService.useCoupon(couponItem.getId(), 10000);
        int discountAmount2 = couponService.useCoupon(couponItem2.getId(), 20000);

        //then
        assertThat(discountAmount).isEqualTo(2000);
        assertThat(discountAmount2).isEqualTo(4600);
    }

    @Test
    void getAvailableCoupons() throws Exception {
        //given
        Customer customer = Customer.builder().build();
        customerRepository.save(customer);

        Coupon coupon_4000off = Coupon.builder().priceOff(4000).build();
        Coupon coupon_max5000 = Coupon.builder()
                .name("coupon_max5000")
                .percentOff(23)
                .maxPriceOff(5000)
                .expiredDate(LocalDateTime.now().plusDays(30))
                .build();
        couponRepository.save(coupon_4000off);
        couponRepository.save(coupon_max5000);

        CouponItem couponItem = CouponItem.builder().coupon(coupon_4000off).customer(customer).build();
        CouponItem couponItem2 = CouponItem.builder().coupon(coupon_max5000).customer(customer).build();
        couponItemRepository.save(couponItem);
        couponItemRepository.save(couponItem2);

        //when
        List<CouponItemDto> availableCoupons = couponService.getAvailableCoupons(customer.getId(), 3000);

        //then
        assertThat(availableCoupons.size()).isEqualTo(1);
        assertThat(availableCoupons.get(0).getCouponDto().getName()).isEqualTo("coupon_max5000");
    }

}
