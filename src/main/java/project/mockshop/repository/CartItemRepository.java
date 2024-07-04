package project.mockshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.mockshop.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
