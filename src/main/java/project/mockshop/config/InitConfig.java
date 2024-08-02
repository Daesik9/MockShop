package project.mockshop.config;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.mockshop.dto.CouponDto;
import project.mockshop.dto.EventCreationDto;
import project.mockshop.dto.EventRewardDto;
import project.mockshop.dto.ItemDto;
import project.mockshop.entity.Address;
import project.mockshop.entity.Customer;
import project.mockshop.entity.Item;
import project.mockshop.mapper.CustomerMapper;
import project.mockshop.service.CouponService;
import project.mockshop.service.CustomerService;
import project.mockshop.service.EventService;
import project.mockshop.service.ItemService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Profile("local")
@RequiredArgsConstructor
public class InitConfig {
    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.init();
    }

    @Service
    @RequiredArgsConstructor
    static class InitService {
        private final ItemService itemService;
        private final CustomerService customerService;
        private final EventService eventService;
        private final CouponService couponService;
        private final EntityManager em;

        @Transactional
        public void saveAll(List<?> entities) {
            int batchSize = 1000;
            for (int i = 0; i < entities.size(); i++) {
                em.persist(entities.get(i));
                if (i % batchSize == 0 && i > 0) {
                    em.flush();
                    em.clear();
                }
            }
            em.flush();
            em.clear();
        }

        @Transactional
        public void init() {
            List<Item> items = new ArrayList<>();
            for (int i = 0; i < 10000; i++) {
                Item item = Item.builder()
                        .name("사과")
                        .price(1000 * (i + 1))
                        .quantity(100)
                        .percentOff(i % 100 == 0 ? 10 : 0)
                        .build();
                items.add(item);
//            itemService.createItem(itemDto, 1L);
            }

            saveAll(items);

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
}
