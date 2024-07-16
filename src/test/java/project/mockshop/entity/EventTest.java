package project.mockshop.entity;

import org.junit.jupiter.api.Test;
import project.mockshop.policy.MockShopPolicy;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;


public class EventTest {

    @Test
    void create() throws Exception {
        //given

        //when
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now().plusDays(10);
        Event event = Event.builder()
                .id(1L)
                .name("이벤트")
                .photo("event-img.png")
                .maxParticipants(100)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        //then
        assertThat(event.getId()).isEqualTo(1L);
        assertThat(event.getName()).isEqualTo("이벤트");
        assertThat(event.getMaxParticipants()).isEqualTo(100);
        assertThat(event.getStartDate()).isEqualTo(startDate);
        assertThat(event.getEndDate()).isEqualTo(endDate);
    }

    @Test
    void create_fail_LessThanOrEqualToZero() throws Exception {
        //given

        //when

        //then
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now().plusDays(10);
        assertThatThrownBy(() -> Event.builder()
                .id(1L)
                .name("이벤트")
                .photo("event-img.png")
                .maxParticipants(-100)
                .startDate(startDate)
                .endDate(endDate)
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining(MockShopPolicy.INPUT_STRING_METHOD("최대 참가 인원"));

        assertThatThrownBy(() -> Event.builder()
                .id(1L)
                .name("이벤트")
                .photo("event-img.png")
                .maxParticipants(0)
                .startDate(startDate)
                .endDate(endDate)
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(MockShopPolicy.INPUT_STRING_METHOD("최대 참가 인원"));
    }

    @Test
    void create_fail_StartDateAfterEnd() throws Exception {
        //given

        //when

        //then
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.minusNanos(1);

        assertThatThrownBy(() -> Event.builder()
                .id(1L)
                .name("이벤트")
                .photo("event-img.png")
                .maxParticipants(10)
                .startDate(startDate)
                .endDate(endDate)
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(MockShopPolicy.INPUT_STRING_METHOD("이벤트 기간"));

        assertThatThrownBy(() -> Event.builder()
                .id(1L)
                .name("이벤트")
                .photo("event-img.png")
                .maxParticipants(10)
                .startDate(startDate)
                .endDate(startDate)
                .build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(MockShopPolicy.INPUT_STRING_METHOD("이벤트 기간"));
    }

}