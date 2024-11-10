package project.mockshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.mockshop.entity.Customer;
import project.mockshop.entity.Order;
import project.mockshop.entity.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {
    List<Order> findAllByCustomerId(Long customerId);

    List<Order> findAllByCustomerIdOrderByOrderDateDesc(Long customerId);

    List<Order> findAllByCustomerName(String customerName);

    List<Order> findAllByCustomerLoginId(String customerLoginId);

    List<Order> findAllByCustomerPhoneNumber(String customerPhoneNumber);

    Optional<Order> findByOrderNumber(String orderNumber);

    List<Order> findAllByStatus(OrderStatus status);

    List<Order> findAllByOrderDateBetween(LocalDateTime from, LocalDateTime to);
}
