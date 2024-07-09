package project.mockshop.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.mockshop.dto.CartItemDto;
import project.mockshop.entity.Cart;
import project.mockshop.entity.CartItem;
import project.mockshop.entity.Customer;
import project.mockshop.entity.Item;
import project.mockshop.mapper.CartItemMapper;
import project.mockshop.repository.CartRepository;
import project.mockshop.repository.ItemRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final ItemRepository itemRepository;

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

    public void addToCart(Long itemId, int count, Long userId) {
        Optional<Cart> cartOptional = cartRepository.findByCustomerId(userId);
        Optional<Item> itemOptional = itemRepository.findById(itemId);
        if (itemOptional.isEmpty()) {
            throw new NullPointerException("해당 상품이 없습니다.");
        }

        //사용자가 기존 장바구니가 있는 경우
        if (cartOptional.isPresent()) {
            Optional<CartItem> findSameCartItem = cartOptional.get().getCartItems().stream()
                    .filter(cartItem -> cartItem.getItem().getId().equals(itemId))
                    .findAny();
            //기존 장바구니에 같은 상품이 있는 경우
            if (findSameCartItem.isPresent()) {
                findSameCartItem.get().changeCount(count);
            } else {
                CartItem cartItem = CartItem.createCartItem(itemOptional.get(), count);
                cartItem.changeCart(cartOptional.get());
                cartOptional.get().getCartItems().add(cartItem);
            }
        } else {
            CartItem cartItem = CartItem.createCartItem(itemOptional.get(), count);
            Cart cart = Cart.builder().customer(Customer.builder().id(userId).build()).cartItems(List.of(cartItem)).build();
            cartRepository.save(cart);
        }
    }

    public List<CartItemDto> getCartItems(Long customerId) {
        Cart cart = cartRepository.findCartWithItems(customerId);

        return cart.getCartItems().stream().map(CartItemMapper::toDto).toList();
    }
}
