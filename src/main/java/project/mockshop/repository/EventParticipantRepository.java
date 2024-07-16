package project.mockshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.mockshop.entity.EventParticipant;

import java.util.Optional;

public interface EventParticipantRepository extends JpaRepository<EventParticipant, Long> {
    Optional<EventParticipant> findByEventIdAndCustomerId(Long eventId, Long customerId);

    int countByEventId(Long eventId);
}
