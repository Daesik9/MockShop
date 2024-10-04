package project.mockshop.service;

import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import project.mockshop.dto.CouponDto;
import project.mockshop.dto.CouponItemDto;
import project.mockshop.entity.Coupon;
import project.mockshop.entity.CouponItem;
import project.mockshop.entity.Customer;
import project.mockshop.entity.Item;
import project.mockshop.mapper.CouponMapper;
import project.mockshop.policy.MockShopPolicy;
import project.mockshop.repository.CouponItemRepository;
import project.mockshop.repository.CouponRepository;
import project.mockshop.repository.CustomerRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CouponServiceTest {
    @InjectMocks
    CouponService couponService;

    @Mock
    CouponRepository couponRepository;
    @Mock
    CustomerRepository customerRepository;
    @Mock
    CouponItemRepository couponItemRepository;


    @Test
    void serviceRepositoryNotNull() throws Exception {
        //given

        //when

        //then
        assertThat(couponService).isNotNull();
        assertThat(couponRepository).isNotNull();
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
        when(couponRepository.findById(couponId))
                .thenReturn(Optional.ofNullable(CouponMapper.toEntity(couponDtoPriceOff)));

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

        //when
        when(couponItemRepository.findAllByCustomerId(customer.getId())).thenReturn(List.of(couponItem));
        List<CouponItemDto> couponItemDtos = couponService.getAllCouponItemsByCustomerId(customer.getId());

        //then
        assertThat(couponItemDtos.size()).isEqualTo(1);
    }

    @Test
    void issueCoupon() throws Exception {
        //given
        Coupon coupon = Coupon.builder().build();
        Customer customer = Customer.builder().build();
        given(customerRepository.findById(customer.getId())).willReturn(Optional.of(customer));
        given(couponRepository.findById(coupon.getId())).willReturn(Optional.of(coupon));
        CouponItem couponItem = CouponItem.builder().coupon(coupon).customer(customer).build();

        //when
        Long couponItemId = couponService.issueCoupon(coupon.getId(), customer.getId());
        given(couponItemRepository.findAllByCustomerId(customer.getId())).willReturn(List.of(couponItem));

        //then
        List<CouponItem> couponItems = couponItemRepository.findAllByCustomerId(customer.getId());
        assertThat(couponItems.size()).isEqualTo(1);
        verify(couponItemRepository, times(1)).save(any(CouponItem.class));
    }


}