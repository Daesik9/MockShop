package project.mockshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.mockshop.entity.CouponItem;

import java.util.List;

public interface CouponItemRepository extends JpaRepository<CouponItem, Long> {

    List<CouponItem> findAllByCustomerId(Long customerId);
}
