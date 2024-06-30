package project.mockshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.mockshop.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
}