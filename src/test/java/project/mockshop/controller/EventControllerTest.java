package project.mockshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import project.mockshop.advice.ExceptionAdvice;
import project.mockshop.dto.EventCreationDto;
import project.mockshop.dto.EventParticipationDto;
import project.mockshop.dto.EventRewardDto;
import project.mockshop.service.EventService;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class EventControllerTest {

    @InjectMocks
    private EventController eventController;
    @Mock
    private EventService eventService;
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
        EventParticipationDto eventParticipationDto  = EventParticipationDto.builder()
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


}