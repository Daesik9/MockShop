package project.mockshop.entity;

import jakarta.persistence.*;
import lombok.*;
import project.mockshop.policy.MockShopPolicy;

@Builder
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CartItem {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private Item item;
    private int cartPrice;
    private int count;

    public void changeCount(int count) {
        if (count > item.getQuantity()) {
            throw new IllegalStateException("재고가 부족합니다.");
        }
        this.count = count;
    }

    public static class CartItemBuilder {
        public CartItemBuilder cartPrice(int cartPrice) {
            if (cartPrice < 0) {
                throw new IllegalArgumentException(MockShopPolicy.INPUT_STRING_METHOD("가격"));
            }
            this.cartPrice = cartPrice;
            return this;
        }

        public CartItemBuilder count(int count) {
            if (count < 0) {
                throw new IllegalArgumentException(MockShopPolicy.INPUT_STRING_METHOD("수량"));
            }
            this.count = count;
            return this;
        }
    }
}
