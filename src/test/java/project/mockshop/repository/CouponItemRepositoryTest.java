package project.mockshop.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import project.mockshop.entity.CouponItem;
import project.mockshop.entity.Customer;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class CouponItemRepositoryTest {
    @Autowired
    private CouponItemRepository couponItemRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void notNull() throws Exception {
        //given

        //when

        //then
        assertThat(couponItemRepository).isNotNull();
    }

    @Test
    void findAllByCustomerId() throws Exception {
        //given
        Customer customer = Customer.builder().build();
        CouponItem couponItem = CouponItem.builder().customer(customer).build();
        customerRepository.save(customer);
        couponItemRepository.save(couponItem);

        //when
        List<CouponItem> couponItems = couponItemRepository.findAllByCustomerId(customer.getId());

        //then
        assertThat(couponItems.size()).isEqualTo(1);
    }




}