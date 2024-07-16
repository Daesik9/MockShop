package project.mockshop.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import project.mockshop.policy.MockShopPolicy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Entity
public class Event {
    @Id @GeneratedValue
    private Long id;
    private String name;
    private String photo;
    private int maxParticipants;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventReward> eventRewards;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventParticipant> participants;

    public void participateEvent(Customer customer) {
        EventParticipant eventParticipant = EventParticipant.builder()
                .event(this)
                .customer(customer)
                .build();

        this.participants.add(eventParticipant);
    }

    public static class EventBuilder {
        public EventBuilder maxParticipants(int maxParticipants) {
            if (maxParticipants <= 0) {
                throw new IllegalArgumentException(MockShopPolicy.INPUT_STRING_METHOD("최대 참가 인원"));
            }
            this.maxParticipants = maxParticipants;
            return this;
        }

        public EventBuilder participants(List<EventParticipant> participants) {
            this.participants = new ArrayList<>();
            this.participants.addAll(participants);
            return this;
        }

        public Event build() {
            Event event = new Event(id, name, photo, maxParticipants, startDate, endDate, eventRewards, participants);

            if (event.startDate.isEqual(event.endDate) || event.startDate.isAfter(event.endDate)) {
                throw new IllegalArgumentException(MockShopPolicy.INPUT_STRING_METHOD("이벤트 기간"));
            }

            for (EventReward eventReward : event.eventRewards) {
                eventReward.changeEvent(event);
            }

            if (event.participants == null) {
                event.participants = new ArrayList<>();
            }

            return event;
        }
    }
}
