package project.mockshop.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * coupon_item	id
 * coupon_id
 * customer_id
 * use_yn
 */
public class CouponItemTest {

    @Test
    void couponItem() throws Exception {
        //given

        //when
        CouponItem couponItem = CouponItem.builder()
                .id(1L)
                .coupon(Coupon.builder().build())
                .customer(Customer.builder().build())
                .isUsed(false)
                .build();

        //then
        assertThat(couponItem.getId()).isEqualTo(1L);
        assertThat(couponItem.isUsed()).isEqualTo(false);
    }

    @Test
    void changeIsUsed() throws Exception {
        //given
        CouponItem couponItem = CouponItem.builder()
                .id(1L)
                .coupon(Coupon.builder().build())
                .customer(Customer.builder().build())
                .isUsed(false)
                .build();

        //when
        couponItem.changeIsUsed(true);

        //then
        assertThat(couponItem.isUsed()).isEqualTo(true);
    }



}