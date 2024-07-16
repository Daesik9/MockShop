package project.mockshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import project.mockshop.dto.EventCreationDto;
import project.mockshop.dto.EventParticipationDto;
import project.mockshop.response.Response;
import project.mockshop.service.EventService;

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

    @PostMapping("/events/{eventId}/participants")
    public Response participateEvent(@PathVariable Long eventId, @RequestBody EventParticipationDto participationDto) {
        eventService.participateEvent(participationDto.getCustomerId(), eventId);

        return Response.success();
    }

}
