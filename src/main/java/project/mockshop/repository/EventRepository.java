package project.mockshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.mockshop.entity.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>, EventRepositoryCustom {
    List<Event> findAllByDateTime(LocalDateTime dateTime);
}

