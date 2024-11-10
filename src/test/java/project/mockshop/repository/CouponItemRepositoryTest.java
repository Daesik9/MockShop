package project.mockshop.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import project.mockshop.entity.Coupon;
import project.mockshop.entity.CouponItem;
import project.mockshop.entity.Customer;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class CouponItemRepositoryTest {
    @Autowired
    private CouponItemRepository couponItemRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CouponRepository couponRepository;

    @Test
    void notNull() throws Exception {
        //given

        //when

        //then
        assertThat(couponItemRepository).isNotNull();
    }

    @Test
    void findAllByCustomerId() throws Exception {
        //given
        Customer customer = Customer.builder().build();
        CouponItem couponItem = CouponItem.builder().customer(customer).build();
        customerRepository.save(customer);
        couponItemRepository.save(couponItem);

        //when
        List<CouponItem> couponItems = couponItemRepository.findAllByCustomerId(customer.getId());

        //then
        assertThat(couponItems.size()).isEqualTo(1);
    }

    @Test
    void findAvailableCouponsByCustomerId() throws Exception {
        //given
        Coupon couponPriceOff = Coupon.builder()
                .name("coupon")
                .priceOff(4000)
                .expiredDate(LocalDateTime.now().plusDays(30))
                .minPriceRequired(5000)
                .build();
        Coupon couponPercentOff = Coupon.builder()
                .name("coupon2")
                .percentOff(10)
                .expiredDate(LocalDateTime.now().plusDays(15))
                .minPriceRequired(1000)
                .maxPriceOff(2000)
                .build();
        couponRepository.save(couponPriceOff);
        couponRepository.save(couponPercentOff);

        Customer customer = Customer.builder().build();
        CouponItem couponItem1 = CouponItem.builder()
                .customer(customer)
                .coupon(couponPercentOff)
                .build();
        CouponItem couponItem2 = CouponItem.builder()
                .customer(customer)
                .coupon(couponPriceOff)
                .build();
        customerRepository.save(customer);
        couponItemRepository.save(couponItem1);
        couponItemRepository.save(couponItem2);

        //when
        List<CouponItem> couponItems = couponItemRepository.findAvailableCouponsByCustomerIdAndTotalPrice(customer.getId(), 3000);

        //then
        assertThat(couponItems.size()).isEqualTo(1);
        assertThat(couponItems.get(0).getCoupon().getName()).isEqualTo("coupon2");
    }

}
