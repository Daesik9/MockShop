package project.mockshop.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import project.mockshop.entity.*;

import java.time.LocalDateTime;
import java.util.List;

public class CouponItemRepositoryImpl implements CouponItemRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public CouponItemRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<CouponItem> findAvailableCouponsByCustomerIdAndTotalPrice(Long customerId, Integer totalPrice) {
        QCouponItem couponItem = QCouponItem.couponItem;

        return queryFactory.selectFrom(couponItem)
                .leftJoin(couponItem.coupon, QCoupon.coupon).fetchJoin()
                .leftJoin(couponItem.customer, QCustomer.customer).fetchJoin()
                .where(couponItem.customer.id.eq(customerId))
                .where(couponItem.isUsed.eq(false))
                .where(couponItem.coupon.expiredDate.after(LocalDateTime.now()))
                .where(couponItem.coupon.minPriceRequired.loe(totalPrice))
                .where(couponItem.coupon.priceOff.loe(totalPrice)) //만약 percentOff면 priceOff가 0이니깐 만족함
                .fetch();
    }
}
