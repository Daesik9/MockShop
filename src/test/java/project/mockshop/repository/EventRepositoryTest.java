package project.mockshop.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class EventRepositoryTest {
    @Autowired
    private EventRepository eventRepository;

    @Test
    void notNull() throws Exception {
        //given

        //when

        //then
        assertThat(eventRepository).isNotNull();
    }

    //이벤트 참석 여부

    //이벤트 참석 (선착순 아직 괜찮은지)

    //


}