package project.mockshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.mockshop.dto.EventCreationDto;
import project.mockshop.dto.EventRewardDto;
import project.mockshop.entity.*;
import project.mockshop.repository.CouponRepository;
import project.mockshop.repository.CustomerRepository;
import project.mockshop.repository.EventParticipantRepository;
import project.mockshop.repository.EventRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class EventService {

    private final EventRepository eventRepository;
    private final CouponRepository couponRepository;
    private final CustomerRepository customerRepository;
    private final EventParticipantRepository eventParticipantRepository;
    private final CouponService couponService;

    public Long createEvent(EventCreationDto creationDto) {
        List<EventRewardDto> eventRewardDtos = creationDto.getEventRewardDtos();
        List<EventReward> eventRewards = new ArrayList<>();
        for (EventRewardDto eventRewardDto : eventRewardDtos) {
            int count = eventRewardDto.getCount();
            Long couponId = eventRewardDto.getCouponId();
            Coupon coupon = couponRepository.findById(couponId)
                    .orElseThrow(() -> new NoSuchElementException("해당 쿠폰이 없습니다."));
            EventReward eventReward = EventReward.builder().count(count).coupon(coupon).build();
            eventRewards.add(eventReward);
        }


        Event event = Event.builder()
                .name(creationDto.getName())
                .photo(creationDto.getPhoto())
                .maxParticipants(creationDto.getMaxParticipationNumber())
                .startDate(creationDto.getStartDate())
                .endDate(creationDto.getEndDate())
                .eventRewards(eventRewards)
                .build();


        eventRepository.save(event);
        return event.getId();
    }

    public void participateEvent(Long customerId, Long eventId) {
        LocalDateTime now = LocalDateTime.now();
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NoSuchElementException("해당 이벤트가 없습니다."));
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new NoSuchElementException("해당 구매자가 없습니다."));

        if (event.getStartDate().isAfter(now) || event.getEndDate().isBefore(now)) {
            throw new IllegalArgumentException("현재 이벤트 기간이 아닙니다.");
        }

        Optional<EventParticipant> eventParticipant = eventParticipantRepository.findByEventIdAndCustomerId(eventId, customerId);
        if (eventParticipant.isPresent()) {
            throw new IllegalArgumentException("이미 참여한 이벤트입니다.");
        }

        int participantCount = eventParticipantRepository.countByEventId(eventId);
        if (participantCount >= event.getMaxParticipants()) {
            throw new IllegalArgumentException("이미 선착순 마감된 이벤트입니다.");
        }

        event.participateEvent(customer);

        //쿠폰 발급
        for (EventReward eventReward : event.getEventRewards()) {
            Coupon coupon = eventReward.getCoupon();
            int count = eventReward.getCount();
            for (int i = 0; i < count; i++) {
                couponService.issueCoupon(coupon.getId(), customerId);
            }
        }

    }
}
