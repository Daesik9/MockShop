package project.mockshop.repository;

import project.mockshop.entity.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepositoryCustom {
    List<Event> findAllByDateTime(LocalDateTime dateTime);
}
