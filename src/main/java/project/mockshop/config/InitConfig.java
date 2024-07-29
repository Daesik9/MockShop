package project.mockshop.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import project.mockshop.dto.CouponDto;
import project.mockshop.dto.EventCreationDto;
import project.mockshop.dto.EventRewardDto;
import project.mockshop.dto.ItemDto;
import project.mockshop.entity.Address;
import project.mockshop.entity.Customer;
import project.mockshop.mapper.CustomerMapper;
import project.mockshop.service.CouponService;
import project.mockshop.service.CustomerService;
import project.mockshop.service.EventService;
import project.mockshop.service.ItemService;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Profile("local")
public class InitConfig {
    private final ItemService itemService;
    private final CustomerService customerService;
    private final EventService eventService;
    private final CouponService couponService;

    @PostConstruct
    public void init() {
        for (int i = 0; i < 20; i++) {
            ItemDto itemDto = ItemDto.builder().name("사과").price(1000 * (i + 1)).quantity(100).build();
            itemService.createItem(itemDto, 1L);
        }

        Customer customer = Customer.builder()
                .name("테스트")
                .password("Password1!")
                .email("test@gmail.com")
                .loginId("test")
                .phoneNumber("01011111111")
                .address(new Address("city", "street", "11111"))
                .build();
        Customer customer2 = Customer.builder()
                .name("테스트둘")
                .password("Password1!")
                .email("test@gmail.com")
                .loginId("test2")
                .phoneNumber("01011111111")
                .address(new Address("city", "street", "11111"))
                .build();
        customerService.createAccount(CustomerMapper.toCreationDto(customer));
        customerService.createAccount(CustomerMapper.toCreationDto(customer2));


        LocalDateTime now = LocalDateTime.now();

        CouponDto couponDtoPriceOff = CouponDto.builder()
                .name("coupon")
                .priceOff(1000)
                .expiredDate(now.plusDays(30))
                .minPriceRequired(1000)
                .build();
        CouponDto couponDtoPercentOff = CouponDto.builder()
                .name("coupon2")
                .percentOff(10)
                .expiredDate(now.plusDays(15))
                .minPriceRequired(1000)
                .maxPriceOff(2000)
                .build();
        Long couponId1 = couponService.createCoupon(couponDtoPriceOff);
        Long couponId2 = couponService.createCoupon(couponDtoPercentOff);

        EventCreationDto eventCreationDto = EventCreationDto.builder()
                .name("이벤트1")
                .photo("photo1.png")
                .maxParticipationNumber(1)
                .startDate(now.minusDays(1))
                .endDate(now.plusDays(1))
                .eventRewardDtos(List.of(EventRewardDto.builder().couponId(couponId1).count(1).build()))
                .build();
        EventCreationDto eventCreationDto2 = EventCreationDto.builder()
                .name("이벤트2")
                .photo("photo2.png")
                .maxParticipationNumber(2)
                .startDate(now.minusDays(2))
                .endDate(now.plusDays(2))
                .eventRewardDtos(List.of(EventRewardDto.builder().couponId(couponId1).count(2).build(),
                        EventRewardDto.builder().couponId(couponId2).count(3).build()))
                .build();
        eventService.createEvent(eventCreationDto);
        eventService.createEvent(eventCreationDto2);
    }

}
