package project.mockshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import project.mockshop.advice.ExceptionAdvice;
import project.mockshop.dto.EventCreationDto;
import project.mockshop.dto.EventDto;
import project.mockshop.dto.EventParticipationDto;
import project.mockshop.dto.EventRewardDto;
import project.mockshop.entity.Event;
import project.mockshop.entity.EventReward;
import project.mockshop.repository.EventRepository;
import project.mockshop.service.EventService;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class EventControllerTest {

    @InjectMocks
    private EventController eventController;
    @Mock
    private EventService eventService;
    @Mock
    private EventRepository eventRepository;
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(eventController)
                .setControllerAdvice(new ExceptionAdvice())
                .build();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    void notNull() throws Exception {
        //given

        //when

        //then
        assertThat(eventController).isNotNull();
        assertThat(eventService).isNotNull();
    }

    @Test
    void createEvent() throws Exception {
        //given
        EventCreationDto eventCreationDto = EventCreationDto.builder()
                .name("이벤트")
                .photo("event_banner.png")
                .maxParticipationNumber(100)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(10))
                .eventRewardDtos(List.of(EventRewardDto.builder().couponId(1L).count(10).build()))
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventCreationDto))
        );

        //then
        resultActions.andExpect(jsonPath("$.code").value(201));
    }

    @Test
    void participateEvent() throws Exception {
        //given
        Long eventId = 1L;
        EventParticipationDto eventParticipationDto = EventParticipationDto.builder()
                .customerId(1L)
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/api/events/{eventId}/participants", eventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventParticipationDto))
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    void getOnGoingEvents() throws Exception {
        //given
        LocalDateTime dateTime = LocalDateTime.now();
        given(eventService.getOnGoingEvents())
                .willReturn(List.of(EventDto.builder()
                        .name("이벤트")
                        .startDate(dateTime.minusDays(3))
                        .endDate(dateTime.plusDays(3))
                        .build()));

        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/events/ongoing")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.result.data[0].name").value("이벤트"));
    }

    @Test
    void getEventDetail() throws Exception {
        //given
        LocalDateTime dateTime = LocalDateTime.now();
        Long eventId = 1L;
        given(eventService.getEventDetail(eventId))
                .willReturn(EventDto.builder()
                        .name("이벤트")
                        .startDate(dateTime.minusDays(3))
                        .endDate(dateTime.plusDays(3))
                        .build());

        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/events/{eventId}", eventId)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.result.data.name").value("이벤트"));
    }


}