package project.mockshop.config;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.mockshop.dto.CouponDto;
import project.mockshop.dto.EventCreationDto;
import project.mockshop.dto.EventRewardDto;
import project.mockshop.service.CouponService;
import project.mockshop.service.EventService;

import java.time.LocalDateTime;
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
        private final EventService eventService;
        private final CouponService couponService;
        private final EntityManager em;

//        @Transactional
//        public void saveAll(List<?> entities) {
//            int batchSize = 1000;
//            for (int i = 0; i < entities.size(); i++) {
//                em.persist(entities.get(i));
//                if (i % batchSize == 0 && i > 0) {
//                    em.flush();
//                    em.clear();
//                }
//            }
//            em.flush();
//            em.clear();
//        }

        @Transactional
        public void init() {
            LocalDateTime now = LocalDateTime.now();

            CouponDto couponDtoPriceOff = CouponDto.builder()
                    .name("coupon")
                    .priceOff(4000)
                    .expiredDate(now.plusDays(30))
                    .minPriceRequired(5000)
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
                    .startDate(now.minusDays(1).withHour(14).withMinute(0).withSecond(0))
                    .endDate(now.plusDays(1).withHour(14).withMinute(0).withSecond(0))
                    .eventRewardDtos(List.of(EventRewardDto.builder().couponId(couponId1).count(3).build(),
                            EventRewardDto.builder().couponId(couponId2).count(2).build()))
                    .build();
            EventCreationDto eventCreationDto2 = EventCreationDto.builder()
                    .name("이벤트2")
                    .photo("photo2.png")
                    .maxParticipationNumber(10)
                    .startDate(now.plusDays(2).withHour(14).withMinute(0).withSecond(0))
                    .endDate(now.plusDays(2).withHour(16).withMinute(0).withSecond(0))
                    .eventRewardDtos(List.of(EventRewardDto.builder().couponId(couponId1).count(2).build(),
                            EventRewardDto.builder().couponId(couponId2).count(3).build()))
                    .build();
            eventService.createEvent(eventCreationDto);
            eventService.createEvent(eventCreationDto2);
        }


    }
}
