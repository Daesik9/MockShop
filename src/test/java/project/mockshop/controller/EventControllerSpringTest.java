package project.mockshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import project.mockshop.annotation.WithMockMember;
import project.mockshop.dto.*;
import project.mockshop.entity.AddressInfo;
import project.mockshop.repository.EventRepository;
import project.mockshop.service.CouponService;
import project.mockshop.service.CustomerService;
import project.mockshop.service.EventService;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class EventControllerSpringTest {
    @Autowired
    private EventController eventController;
    @Autowired
    private EventService eventService;
    @Autowired
    private CouponService couponService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void init() {
//        mockMvc = MockMvcBuilders.standaloneSetup(eventController)
//                .setControllerAdvice(new ExceptionAdvice())
//                .build();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    void notNull() throws Exception {
        //given

        //when

        //then
        assertThat(eventController).isNotNull();
    }

    @Test
    @WithMockMember(role = "ROLE_ADMIN")
    void createEvent() throws Exception {
        //given
        CouponDto couponDtoPriceOff = CouponDto.builder()
                .name("coupon")
                .priceOff(1000)
                .expiredDate(LocalDateTime.now().plusDays(30))
                .minPriceRequired(1000)
                .build();
        Long couponId = couponService.createCoupon(couponDtoPriceOff);


        EventCreationDto eventCreationDto = EventCreationDto.builder()
                .name("이벤트")
                .photo("event_banner.png")
                .maxParticipationNumber(100)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(10))
                .eventRewardDtos(List.of(EventRewardDto.builder().couponId(couponId).count(10).build()))
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventCreationDto))
        );

        //then
        resultActions.andExpect(jsonPath("$.code").value(201));

//        MvcResult result = resultActions.andReturn();
//        String responseString = result.getResponse().getContentAsString();
//
//        Response response = objectMapper.readValue(responseString, Response.class);
//        Long createdEventId = response.getResult();
//        assertThat(eventRepository.findById(createdEventId)).isNotNull();
    }

    @Test
    @WithMockMember
    void participateEvent() throws Exception {
        //given
        CustomerCreationDto requestDto = CustomerCreationDto.builder()
                .loginId("loginid")
                .name("테스트")
                .password("Password1!")
                .phoneNumber("01011111111")
                .email("email@email.com")
                .addressInfo(new AddressInfo("city", "street", "88888"))
                .build();

        Long customerId = customerService.createAccount(requestDto);

        CouponDto couponDtoPriceOff = CouponDto.builder()
                .name("coupon")
                .priceOff(1000)
                .expiredDate(LocalDateTime.now().plusDays(30))
                .minPriceRequired(1000)
                .build();
        Long couponId = couponService.createCoupon(couponDtoPriceOff);

        EventCreationDto eventCreationDto = EventCreationDto.builder()
                .name("이벤트")
                .photo("event_banner.png")
                .maxParticipationNumber(100)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(10))
                .eventRewardDtos(List.of(EventRewardDto.builder().couponId(couponId).count(10).build()))
                .build();
        Long eventId = eventService.createEvent(eventCreationDto);


        EventParticipationDto eventParticipationDto  = EventParticipationDto.builder()
                .customerId(customerId)
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

        assertThat(couponService.getAllCouponItemsByCustomerId(customerId).size()).isEqualTo(10);
    }

    @Test
    void getOnGoingEvents() throws Exception {
        //given
        CouponDto couponDtoPriceOff = CouponDto.builder()
                .name("coupon")
                .priceOff(1000)
                .expiredDate(LocalDateTime.now().plusDays(30))
                .minPriceRequired(1000)
                .build();
        Long couponId = couponService.createCoupon(couponDtoPriceOff);

        EventCreationDto eventCreationDto = EventCreationDto.builder()
                .name("이벤트")
                .photo("event_banner.png")
                .maxParticipationNumber(100)
                .startDate(LocalDateTime.now().minusDays(1))
                .endDate(LocalDateTime.now().plusDays(10))
                .eventRewardDtos(List.of(EventRewardDto.builder().couponId(couponId).count(10).build()))
                .build();
        Long eventId = eventService.createEvent(eventCreationDto);

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
        CouponDto couponDtoPriceOff = CouponDto.builder()
                .name("coupon")
                .priceOff(1000)
                .expiredDate(LocalDateTime.now().plusDays(30))
                .minPriceRequired(1000)
                .build();
        Long couponId = couponService.createCoupon(couponDtoPriceOff);

        EventCreationDto eventCreationDto = EventCreationDto.builder()
                .name("이벤트")
                .photo("event_banner.png")
                .maxParticipationNumber(100)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(10))
                .eventRewardDtos(List.of(EventRewardDto.builder().couponId(couponId).count(10).build()))
                .build();
        Long eventId = eventService.createEvent(eventCreationDto);

        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/events/{eventId}", eventId)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.result.data.name").value("이벤트"))
                .andExpect(jsonPath("$.result.data.photo").value("event_banner.png"));
    }

    @Test
    void getAllEvents() throws Exception {
        //given
        CouponDto couponDtoPriceOff = CouponDto.builder()
                .name("coupon")
                .priceOff(1000)
                .expiredDate(LocalDateTime.now().plusDays(30))
                .minPriceRequired(1000)
                .build();
        Long couponId = couponService.createCoupon(couponDtoPriceOff);

        EventCreationDto eventCreationDto1 = EventCreationDto.builder()
                .name("이벤트1")
                .photo("event_banner.png")
                .maxParticipationNumber(100)
                .startDate(LocalDateTime.now().minusDays(1))
                .endDate(LocalDateTime.now().plusDays(1))
                .eventRewardDtos(List.of(EventRewardDto.builder().couponId(couponId).count(10).build()))
                .build();

        EventCreationDto eventCreationDto2 = EventCreationDto.builder()
                .name("이벤트2")
                .photo("event_banner.png")
                .maxParticipationNumber(100)
                .startDate(LocalDateTime.now().plusDays(2))
                .endDate(LocalDateTime.now().plusDays(4))
                .eventRewardDtos(List.of(EventRewardDto.builder().couponId(couponId).count(10).build()))
                .build();

        Long eventId1 = eventService.createEvent(eventCreationDto1);
        Long eventId2 = eventService.createEvent(eventCreationDto2);

        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/events")
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.result.data[0].name").value("이벤트1"))
                .andExpect(jsonPath("$.result.data[1].name").value("이벤트2"));
    }

}