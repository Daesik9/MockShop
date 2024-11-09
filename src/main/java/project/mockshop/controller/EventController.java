package project.mockshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import project.mockshop.dto.EventCreationDto;
import project.mockshop.dto.EventDto;
import project.mockshop.dto.EventParticipationDto;
import project.mockshop.response.Response;
import project.mockshop.service.EventService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
class EventController {
    private final EventService eventService;

    @PostMapping("/events")
    public Response createEvent(@RequestBody EventCreationDto creationDto) {
        Long eventId = eventService.createEvent(creationDto);

        return Response.success(HttpStatus.CREATED.value(), eventId);
    }

    @GetMapping("/events")
    public Response getAllEvents() {
        List<EventDto> allEvents = eventService.getAllEvents();

        return Response.success(allEvents);
    }

    @PostMapping("/events/{eventId}/participants")
    public Response participateEvent(@PathVariable Long eventId, @RequestBody EventParticipationDto participationDto) {
        eventService.participateEvent(participationDto.getCustomerId(), eventId);

        return Response.success();
    }

    @GetMapping("/events/ongoing")
    public Response getOnGoingEvents() {
        List<EventDto> eventDtos = eventService.getOnGoingEvents();
        return Response.success(eventDtos);
    }

    @GetMapping("/events/{eventId}")
    public Response getEventDetail(@PathVariable Long eventId) {
        EventDto eventDto = eventService.getEventDetail(eventId);
        return Response.success(eventDto);
    }

    /**
     * 현재 진행 중인 이벤트들 다 가져오기 -> 배너 세팅 (배너 이미지, 이벤트 id)
     *
     *
     * 이벤트 가져오기 -> 이벤트 상세 페이지 (배너 이미지, 이벤트 id,
     *
     *
     */

}
