package project.mockshop.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Builder
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Cart {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private Customer customer;

    @OneToMany
    @JoinColumn(name = "cart_item_id")
    private List<CartItem> cartItems;

    public void changeCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

}
