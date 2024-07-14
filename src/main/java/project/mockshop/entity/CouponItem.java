package project.mockshop.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@Entity
public class CouponItem {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private boolean isUsed;
}
