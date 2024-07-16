package project.mockshop.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class EventDto {
    private Long id;
    private String name;
    private String photo;
    private int maxParticipants;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
