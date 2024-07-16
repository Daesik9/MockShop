package project.mockshop.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import project.mockshop.dto.CouponItemDto;
import project.mockshop.dto.EventCreationDto;
import project.mockshop.dto.EventRewardDto;
import project.mockshop.entity.*;
import project.mockshop.repository.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {
    @InjectMocks
    private EventService eventService;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private CouponRepository couponRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private EventParticipantRepository eventParticipantRepository;
    @Mock
    private CouponItemRepository couponItemRepository;
    @Mock
    private CouponService couponService;

    @Test
    void notNull() throws Exception {
        //given

        //when

        //then
        assertThat(eventService).isNotNull();
        assertThat(eventRepository).isNotNull();
    }

    @Test
    void createEvent() throws Exception {
        //given
        EventCreationDto creationDto = EventCreationDto.builder()
                .name("이벤트")
                .photo("event_banner.png")
                .maxParticipationNumber(100)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(10))
                .eventRewardDtos(List.of(EventRewardDto.builder().couponId(1L).count(10).build()))
                .build();
        given(couponRepository.findById(1L)).willReturn(Optional.of(Coupon.builder().build()));

        //when
        Long eventId = eventService.createEvent(creationDto);
        when(eventRepository.findById(eventId))
                .thenReturn(Optional.of(Event.builder()
                        .startDate(creationDto.getStartDate())
                        .endDate(creationDto.getEndDate())
                        .eventRewards(List.of(EventReward.builder().build()))
                        .build()));

        //then
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NoSuchElementException("해당 이벤트가 없습니다."));
        assertThat(event.getId()).isEqualTo(eventId);
        assertThat(event.getEventRewards().get(0).getEvent()).isNotNull();
        assertThat(event.getEventRewards().get(0).getEvent().getId()).isEqualTo(eventId);

    }

    @Test
    void participateEvent() throws Exception {
        //given
        Long customerId = 1L;
        Long eventId = 1L;
        Customer customer = Customer.builder().id(customerId).build();
        Event event = Event.builder()
                .id(eventId)
                .maxParticipants(100)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(10))
                .eventRewards(List.of(EventReward.builder().build()))
                .build();
        given(customerRepository.findById(customerId))
                .willReturn(Optional.of(customer));
        given(eventRepository.findById(eventId)).willReturn(Optional.of(event));
        given(couponService.getAllCouponItemsByCustomerId(customerId)).willReturn(List.of(CouponItemDto.builder().build()));

        //when
        eventService.participateEvent(customerId, eventId);

        //then
        Event foundEvent = eventRepository.findById(eventId).orElseThrow(() -> new NoSuchElementException("해당 이벤트가 없습니다."));
        assertThat(foundEvent.getParticipants().size()).isEqualTo(1);

        List<CouponItemDto> couponItemDtos = couponService.getAllCouponItemsByCustomerId(customerId);
        assertThat(couponItemDtos.size()).isEqualTo(1);
    }

    @Test
    void participateEvent_fail_already() throws Exception {
        //given
        Long customerId = 1L;
        Long eventId = 1L;
        Customer customer = Customer.builder().id(customerId).build();
        Event event = Event.builder()
                .id(eventId)
                .maxParticipants(100)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(10))
                .eventRewards(List.of(EventReward.builder().build()))
                .build();
        EventParticipant eventParticipant = EventParticipant.builder()
                .customer(customer)
                .event(event)
                .build();
        given(customerRepository.findById(customerId))
                .willReturn(Optional.of(customer));
        given(eventRepository.findById(eventId)).willReturn(Optional.of(event));
        given(eventParticipantRepository.findByEventIdAndCustomerId(eventId, customerId))
                .willReturn(Optional.of(eventParticipant));
        given(couponService.getAllCouponItemsByCustomerId(customerId)).willReturn(List.of());

        //when

        //then
        assertThatThrownBy(() -> eventService.participateEvent(customerId, eventId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 참여한 이벤트입니다.");

        List<CouponItemDto> couponItemDtos = couponService.getAllCouponItemsByCustomerId(customerId);
        assertThat(couponItemDtos.size()).isEqualTo(0);
    }

    @Test
    void participateEvent_fail_noSpot() throws Exception {
        //given
        Long customerId = 1L;
        Long eventId = 1L;
        Customer customer = Customer.builder().id(customerId).build();
        Event event = Event.builder()
                .id(eventId)
                .maxParticipants(100)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(10))
                .eventRewards(List.of(EventReward.builder().build()))
                .build();
        given(customerRepository.findById(customerId))
                .willReturn(Optional.of(customer));
        given(eventRepository.findById(eventId)).willReturn(Optional.of(event));
        given(eventParticipantRepository.countByEventId(eventId)).willReturn(100);
        given(couponService.getAllCouponItemsByCustomerId(customerId)).willReturn(List.of());

        //when

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
        Long customerId = 1L;
        Long eventId1 = 1L;
        Customer customer = Customer.builder().id(customerId).build();
        Event notStartEvent = Event.builder()
                .id(eventId1)
                .maxParticipants(100)
                .startDate(LocalDateTime.now().plusSeconds(1))
                .endDate(LocalDateTime.now().plusDays(10))
                .eventRewards(List.of(EventReward.builder().build()))
                .build();

        Long eventId2 = 2L;
        Event endEvent = Event.builder()
                .id(eventId2)
                .maxParticipants(100)
                .startDate(LocalDateTime.now().minusDays(10))
                .endDate(LocalDateTime.now().minusSeconds(1))
                .eventRewards(List.of(EventReward.builder().build()))
                .build();
        given(customerRepository.findById(customerId))
                .willReturn(Optional.of(customer));
        given(eventRepository.findById(eventId1)).willReturn(Optional.of(notStartEvent));
        given(eventRepository.findById(eventId2)).willReturn(Optional.of(endEvent));
        given(couponService.getAllCouponItemsByCustomerId(customerId)).willReturn(List.of());

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

}