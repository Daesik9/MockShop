package project.mockshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.mockshop.entity.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

}
