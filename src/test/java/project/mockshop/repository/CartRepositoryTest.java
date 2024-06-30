package project.mockshop.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class CartRepositoryTest {
    @Autowired
    CartRepository cartRepository;

    @Test
    void notNull() throws Exception {
        //given

        //when

        //then
        assertThat(cartRepository).isNotNull();
    }


}