package project.mockshop.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class EventCreationDto {
    private String name;
    private String photo;
    private int maxParticipationNumber;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;
    private List<EventRewardDto> eventRewardDtos;
}
