package project.mockshop.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

//     * even_participant
//     * id
//     * event_id
//     * customer_id

@Entity
@Builder
@Getter
public class EventParticipant {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

}
