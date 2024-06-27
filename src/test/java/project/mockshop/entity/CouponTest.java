package project.mockshop.entity;

import lombok.Builder;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import project.mockshop.policy.MockShopPolicy;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

/*
쿠폰명, 할인 금액 또는 퍼센트, 사용기간, 사용조건을 보여준다.
사용기간이 만료했으면 "기간 만료"라고 빨간색 글씨로 표시한다.
사용조건은 일정 금액 이상 또는 사용 가능한 카테고리를 표시한다.
 */
public class CouponTest {
    @Test
    void createCoupon() throws Exception {
        //given

        //when
        Coupon couponPercentOff = Coupon.builder()
                .name("coupon")
                .percentOff(10)
                .expiredDate(LocalDateTime.now())
                .maxPriceOff(700)
                .minPriceRequired(10000)
                .build();

        Coupon couponPriceOff = Coupon.builder()
                .name("coupon")
                .priceOff(1000)
                .expiredDate(LocalDateTime.now())
                .minPriceRequired(10000)
                .build();

        //then
        assertThat(couponPercentOff.getName()).isEqualTo("coupon");
    }

    @Test
    void createCoupon_fail_onlyOneOff() throws Exception {
        //given

        //when
        assertThatThrownBy(() -> Coupon.builder()
                .name("coupon")
                .priceOff(10)
                .percentOff(10)
                .expiredDate(LocalDateTime.now())
                .minPriceRequired(10000)
                .build()
        )
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("할인율/가격 둘 중 하나만 입력 가능합니다.");
        // -> 서비스로 빼자. -> 레포지토리에 들어갈때 확인 한 번 더.

        //then
    }

    @Test
    void createCoupon_fail_percentRange() throws Exception {
        //given

        //when
        assertThatThrownBy(() -> Coupon.builder()
                .name("coupon")
                .percentOff(-1)
                .expiredDate(LocalDateTime.now())
                .minPriceRequired(10000)
                .maxPriceOff(700)
                .build()
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(MockShopPolicy.INPUT_STRING_METHOD("할인율"));

        assertThatThrownBy(() -> Coupon.builder()
                .name("coupon")
                .percentOff(101)
                .expiredDate(LocalDateTime.now())
                .minPriceRequired(10000)
                .maxPriceOff(700)
                .build()
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(MockShopPolicy.INPUT_STRING_METHOD("할인율"));

        //then
    }

    @Test
    void createCoupon_fail_priceRange() throws Exception {
        //given

        //when
        assertThatThrownBy(() -> Coupon.builder()
                .name("coupon")
                .priceOff(-1)
                .expiredDate(LocalDateTime.now())
                .minPriceRequired(10000)
                .maxPriceOff(700)
                .build()
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(MockShopPolicy.INPUT_STRING_METHOD("할인 가격"));

        //then
    }

    @Test
    void createCoupon_fail_priceOffAndMaxPriceOff() throws Exception {
        //given

        //when
        assertThatThrownBy(() -> Coupon.builder()
                .name("coupon")
                .priceOff(1000)
                .expiredDate(LocalDateTime.now())
                .minPriceRequired(10000)
                .maxPriceOff(700)
                .build()
        )
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("할인 한도 금액은 할인율에만 해당됩니다.");

        //then
    }
}