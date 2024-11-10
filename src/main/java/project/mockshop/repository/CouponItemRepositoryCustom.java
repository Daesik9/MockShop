package project.mockshop.repository;

import project.mockshop.entity.CouponItem;

import java.util.List;

public interface CouponItemRepositoryCustom {
    List<CouponItem> findAvailableCouponsByCustomerIdAndTotalPrice(Long customerId, Integer totalPrice);
}
