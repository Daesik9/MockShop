package project.mockshop.mapper;

import project.mockshop.dto.EventDto;
import project.mockshop.entity.Event;

public class EventMapper {
    public static EventDto toDto(Event event) {
        return EventDto.builder()
                .id(event.getId())
                .name(event.getName())
                .photo(event.getPhoto())
                .maxParticipants(event.getMaxParticipants())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .build();
    }
}
