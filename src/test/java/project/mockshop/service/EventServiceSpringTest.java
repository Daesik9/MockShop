package project.mockshop.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.mockshop.dto.*;
import project.mockshop.entity.*;
import project.mockshop.repository.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@Transactional
public class EventServiceSpringTest {
    @Autowired
    private EventService eventService;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private CouponService couponService;
    @Autowired
    private CustomerService customerService;

    @Test
    void notNull() throws Exception {
        //given

        //when

        //then
        assertThat(eventService).isNotNull();
    }

    @Test
    void createEvent() throws Exception {
        //given
        Long couponId = couponService.createCoupon(CouponDto.builder().build());

        EventCreationDto creationDto = EventCreationDto.builder()
                .name("이벤트")
                .photo("event_banner.png")
                .maxParticipationNumber(100)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(10))
                .eventRewardDtos(List.of(EventRewardDto.builder().couponId(couponId).count(10).build()))
                .build();

        //when
        Long eventId = eventService.createEvent(creationDto);

        //then
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NoSuchElementException("해당 이벤트가 없습니다."));
        assertThat(event.getId()).isEqualTo(eventId);
        assertThat(event.getEventRewards().get(0).getCoupon().getId()).isEqualTo(couponId);
        assertThat(event.getEventRewards().get(0).getEvent()).isNotNull();
        assertThat(event.getEventRewards().get(0).getEvent().getId()).isEqualTo(eventId);
    }

        @Test
        void participateEvent() throws Exception {
            //given
            CustomerCreationDto cusomterCreationDto = CustomerCreationDto.builder()
                    .loginId("loginid")
                    .name("구매자")
                    .password("Password1!")
                    .phoneNumber("01088888888")
                    .email("email@email.com")
                    .address(new Address("city", "street", "88888"))
                    .build();
            Long customerId = customerService.createAccount(cusomterCreationDto);

            Long couponId = couponService.createCoupon(CouponDto.builder().build());
            EventCreationDto eventCreationDto = EventCreationDto.builder()
                    .name("이벤트")
                    .photo("event_banner.png")
                    .maxParticipationNumber(100)
                    .startDate(LocalDateTime.now())
                    .endDate(LocalDateTime.now().plusDays(10))
                    .eventRewardDtos(List.of(EventRewardDto.builder().couponId(couponId).count(10).build()))
                    .build();
            Long eventId = eventService.createEvent(eventCreationDto);

            //when
            eventService.participateEvent(customerId, eventId);

            //then
            Event foundEvent = eventRepository.findById(eventId).orElseThrow(() -> new NoSuchElementException("해당 이벤트가 없습니다."));
            assertThat(foundEvent.getParticipants().size()).isEqualTo(1);

            List<CouponItemDto> couponItemDtos = couponService.getAllCouponItemsByCustomerId(customerId);
            assertThat(couponItemDtos.size()).isEqualTo(10);
            assertThat(couponItemDtos.get(0).getCouponDto().getId()).isEqualTo(couponId);
        }

        @Test
        void participateEvent_fail_already() throws Exception {
            //given
            CustomerCreationDto cusomterCreationDto = CustomerCreationDto.builder()
                    .loginId("loginid")
                    .name("구매자")
                    .password("Password1!")
                    .phoneNumber("01088888888")
                    .email("email@email.com")
                    .address(new Address("city", "street", "88888"))
                    .build();
            Long customerId = customerService.createAccount(cusomterCreationDto);

            Long couponId = couponService.createCoupon(CouponDto.builder().build());
            EventCreationDto eventCreationDto = EventCreationDto.builder()
                    .name("이벤트")
                    .photo("event_banner.png")
                    .maxParticipationNumber(100)
                    .startDate(LocalDateTime.now())
                    .endDate(LocalDateTime.now().plusDays(10))
                    .eventRewardDtos(List.of(EventRewardDto.builder().couponId(couponId).count(10).build()))
                    .build();
            Long eventId = eventService.createEvent(eventCreationDto);

            //when
            eventService.participateEvent(customerId, eventId);

            //then
            assertThatThrownBy(() -> eventService.participateEvent(customerId, eventId))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("이미 참여한 이벤트입니다.");
            List<CouponItemDto> couponItemDtos = couponService.getAllCouponItemsByCustomerId(customerId);
            assertThat(couponItemDtos.size()).isEqualTo(10);
        }

        @Test
        void participateEvent_fail_noSpot() throws Exception {
            //given
            CustomerCreationDto cusomterCreationDto = CustomerCreationDto.builder()
                    .loginId("loginid")
                    .name("구매자")
                    .password("Password1!")
                    .phoneNumber("01088888888")
                    .email("email@email.com")
                    .address(new Address("city", "street", "88888"))
                    .build();
            Long customerId = customerService.createAccount(cusomterCreationDto);

            CustomerCreationDto cusomterCreationDto2 = CustomerCreationDto.builder()
                    .loginId("loginid2")
                    .name("구매자둘")
                    .password("Password1!")
                    .phoneNumber("01077777777")
                    .email("email2@email.com")
                    .address(new Address("city", "street", "88888"))
                    .build();
            Long customerId2 = customerService.createAccount(cusomterCreationDto2);

            Long couponId = couponService.createCoupon(CouponDto.builder().build());
            EventCreationDto eventCreationDto = EventCreationDto.builder()
                    .name("이벤트")
                    .photo("event_banner.png")
                    .maxParticipationNumber(1)
                    .startDate(LocalDateTime.now())
                    .endDate(LocalDateTime.now().plusDays(10))
                    .eventRewardDtos(List.of(EventRewardDto.builder().couponId(couponId).count(10).build()))
                    .build();
            Long eventId = eventService.createEvent(eventCreationDto);

            //when
            eventService.participateEvent(customerId2, eventId);

            //then
            assertThatThrownBy(() -> eventService.participateEvent(customerId, eventId))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("이미 선착순 마감된 이벤트입니다.");

            List<CouponItemDto> couponItemDtos = couponService.getAllCouponItemsByCustomerId(customerId);
            assertThat(couponItemDtos.size()).isEqualTo(0);
        }

        @Test
        void participateEvent_fail_notEventTime() throws Exception {
            //given
            CustomerCreationDto cusomterCreationDto = CustomerCreationDto.builder()
                    .loginId("loginid")
                    .name("구매자")
                    .password("Password1!")
                    .phoneNumber("01088888888")
                    .email("email@email.com")
                    .address(new Address("city", "street", "88888"))
                    .build();
            Long customerId = customerService.createAccount(cusomterCreationDto);

            Long couponId = couponService.createCoupon(CouponDto.builder().build());
            EventCreationDto notStartEvent = EventCreationDto.builder()
                    .name("이벤트")
                    .photo("event_banner.png")
                    .maxParticipationNumber(100)
                    .startDate(LocalDateTime.now().plusSeconds(1))
                    .endDate(LocalDateTime.now().plusDays(10))
                    .eventRewardDtos(List.of(EventRewardDto.builder().couponId(couponId).count(10).build()))
                    .build();
            EventCreationDto endEvent = EventCreationDto.builder()
                    .name("이벤트")
                    .photo("event_banner.png")
                    .maxParticipationNumber(100)
                    .startDate(LocalDateTime.now().minusDays(10))
                    .endDate(LocalDateTime.now().minusSeconds(1))
                    .eventRewardDtos(List.of(EventRewardDto.builder().couponId(couponId).count(10).build()))
                    .build();

            Long eventId1 = eventService.createEvent(notStartEvent);
            Long eventId2 = eventService.createEvent(endEvent);

            //when

            //then
            assertThatThrownBy(() -> eventService.participateEvent(customerId, eventId1))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("현재 이벤트 기간이 아닙니다.");
            assertThatThrownBy(() -> eventService.participateEvent(customerId, eventId2))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("현재 이벤트 기간이 아닙니다.");

            List<CouponItemDto> couponItemDtos = couponService.getAllCouponItemsByCustomerId(customerId);
            assertThat(couponItemDtos.size()).isEqualTo(0);
        }

    @Test
    void getOnGoingEvents() throws Exception {
        //given
        Event event1 = Event.builder()
                .startDate(LocalDateTime.now().minusDays(3))
                .endDate(LocalDateTime.now().plusDays(3))
                .eventRewards(List.of(EventReward.builder().build()))
                .build();
        //이미 끝난 이벤트
        Event event2 = Event.builder()
                .startDate(LocalDateTime.now().minusDays(3))
                .endDate(LocalDateTime.now().minusDays(1))
                .eventRewards(List.of(EventReward.builder().build()))
                .build();
        //아직 시작 안 한 이벤트
        Event event3 = Event.builder()
                .startDate(LocalDateTime.now().plusDays(1))
                .endDate(LocalDateTime.now().plusDays(3))
                .eventRewards(List.of(EventReward.builder().build()))
                .build();
        eventRepository.save(event1);
        eventRepository.save(event2);
        eventRepository.save(event3);

        //when
        List<EventDto> eventDtos = eventService.getOnGoingEvents();

        //then
        assertThat(eventDtos.size()).isEqualTo(1);
    }

    @Test
    void getEventDetail() throws Exception {
        //given
        Event event = Event.builder()
                .name("이벤트")
                .startDate(LocalDateTime.now().minusDays(3))
                .endDate(LocalDateTime.now().plusDays(3))
                .eventRewards(List.of(EventReward.builder().build()))
                .build();
        eventRepository.save(event);

        //when
        EventDto eventDto = eventService.getEventDetail(event.getId());

        //then
        assertThat(eventDto.getName()).isEqualTo("이벤트");
    }

}