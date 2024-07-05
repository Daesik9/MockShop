package project.mockshop.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
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

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems;

    public void changeCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public static class CartBuilder {
        public CartBuilder cartItems(List<CartItem> cartItems) {
            this.cartItems = new ArrayList<>();
            this.cartItems.addAll(cartItems);
            return this;
        }

        public Cart build() {
            Cart cart = new Cart(id, customer, cartItems);

            for (CartItem cartItem : cartItems) {
                cartItem.changeCart(cart);
            }
            return cart;
        }
    }
}
