package project.mockshop.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import project.mockshop.entity.Coupon;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class CouponRepositoryTest {
    @Autowired
    CouponRepository couponRepository;

    @Test
    void repositoryNotNull() throws Exception {
        //given

        //when

        //then
        assertThat(couponRepository).isNotNull();
    }

    @Test
    void create() throws Exception {
        //given
        Coupon couponPercentOff = Coupon.builder()
                .name("coupon")
                .percentOff(10)
                .expiredDate(LocalDateTime.now())
                .maxPriceOff(700)
                .minPriceRequired(10000)
                .build();

        //when
        Coupon savedCoupon = couponRepository.save(couponPercentOff);

        //then
        assertThat(savedCoupon.getId()).isEqualTo(couponPercentOff.getId());
    }

    @Test
    void findById() throws Exception {
        //given
        Coupon couponPercentOff = Coupon.builder()
                .name("coupon")
                .percentOff(10)
                .expiredDate(LocalDateTime.now())
                .maxPriceOff(700)
                .minPriceRequired(10000)
                .build();
        couponRepository.save(couponPercentOff);

        //when
        Coupon findCoupon = couponRepository.findById(couponPercentOff.getId()).orElse(null);


        //then
        assert findCoupon != null;
        assertThat(findCoupon.getName()).isEqualTo("coupon");
    }



    @Test
    void findAll() throws Exception {
        //given
        Coupon coupon1 = Coupon.builder()
                .name("coupon1")
                .percentOff(10)
                .expiredDate(LocalDateTime.now())
                .maxPriceOff(700)
                .minPriceRequired(10000)
                .build();

        Coupon coupon2 = Coupon.builder()
                .name("coupon2")
                .percentOff(10)
                .expiredDate(LocalDateTime.now())
                .maxPriceOff(700)
                .minPriceRequired(10000)
                .build();

        Coupon coupon3 = Coupon.builder()
                .name("coupon3")
                .percentOff(10)
                .expiredDate(LocalDateTime.now())
                .maxPriceOff(700)
                .minPriceRequired(10000)
                .build();

        couponRepository.save(coupon1);
        couponRepository.save(coupon2);
        couponRepository.save(coupon3);

        //when
        List<Coupon> coupons = couponRepository.findAll();

        //then
        assertThat(coupons.size()).isEqualTo(3);
        assertThat(coupons.get(1).getName()).isEqualTo("coupon2");
    }

//    쿠폰은 수정을 하면 안되지 않나?
//    @Test
//    void update() throws Exception {
//        //given
//        Coupon coupon = Coupon.builder()
//                .name("coupon")
//                .percentOff(10)
//                .expiredDate(LocalDateTime.now())
//                .maxPriceOff(700)
//                .minPriceRequired(10000)
//                .build();
//
//        couponRepository.save(coupon);
//
//        //when
//        Coupon byId = couponRepository.findById(coupon.getId()).orElse(null);
//
//        //then
//        assertThat(1).isEqualTo(2);
//    }

    @Test
    void delete() throws Exception {
        //given
        Coupon coupon1 = Coupon.builder()
                .name("coupon1")
                .percentOff(10)
                .expiredDate(LocalDateTime.now())
                .maxPriceOff(700)
                .minPriceRequired(10000)
                .build();

        Coupon coupon2 = Coupon.builder()
                .name("coupon2")
                .percentOff(10)
                .expiredDate(LocalDateTime.now())
                .maxPriceOff(700)
                .minPriceRequired(10000)
                .build();

        Coupon coupon3 = Coupon.builder()
                .name("coupon3")
                .percentOff(10)
                .expiredDate(LocalDateTime.now())
                .maxPriceOff(700)
                .minPriceRequired(10000)
                .build();

        couponRepository.save(coupon1);
        couponRepository.save(coupon2);
        couponRepository.save(coupon3);

        //when
        couponRepository.delete(coupon2);

        //then
        assertThat(couponRepository.findAll().size()).isEqualTo(2);
        assertThat(couponRepository.findAll().get(1)).isEqualTo(coupon3);
    }

}