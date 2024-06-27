package project.mockshop.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import project.mockshop.policy.MockShopPolicy;

import java.time.LocalDateTime;

@Builder
@Getter
@Entity
public class Coupon {
    @Id @GeneratedValue
    private Long id;

    private String name;
    private int priceOff;
    private double percentOff;
    private LocalDateTime expiredDate;
    private int minPriceRequired;
    private int maxPriceOff;

    public static class CouponBuilder {
        public CouponBuilder percentOff(double percentOff) {
            if (percentOff < 0 || percentOff > 100) {
                throw new IllegalArgumentException(MockShopPolicy.INPUT_STRING_METHOD("할인율"));
            }
            this.percentOff = percentOff;
            return this;
        }

        public CouponBuilder priceOff(int priceOff) {
            if (priceOff < 0) {
                throw new IllegalArgumentException(MockShopPolicy.INPUT_STRING_METHOD("할인 가격"));
            }
            this.priceOff = priceOff;
            return this;
        }

        public Coupon build() {
            if (this.percentOff != 0 && this.priceOff != 0) {
                throw new IllegalStateException("할인율/가격 둘 중 하나만 입력 가능합니다.");
            }

            if (this.maxPriceOff != 0 && this.priceOff != 0) {
                throw new IllegalStateException("할인 한도 금액은 할인율에만 해당됩니다.");
            }

            return new Coupon(id, name, priceOff, percentOff, expiredDate, minPriceRequired, maxPriceOff);
        }
    }
}