package project.mockshop.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.mockshop.entity.Cart;
import project.mockshop.entity.CartItem;
import project.mockshop.repository.CartRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final CartRepository cartRepository;

    public void changeCartItemCount(Long cartId, Long cartItemId, int count) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new RuntimeException("카트가 없습니다."));

        cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getId().equals(cartItemId))
                .findFirst()
                .ifPresent(cartItem -> cartItem.changeCount(count));
    }

    public void removeCartItem(Long cartId, Long cartItemId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new RuntimeException("카트가 없습니다."));

        List<CartItem> cartItems = new ArrayList<>(cart.getCartItems());
        cartItems.removeIf(cartItem -> cartItem.getId().equals(cartItemId));

        cart.changeCartItems(cartItems);
    }
}
