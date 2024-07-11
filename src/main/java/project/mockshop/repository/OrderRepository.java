package project.mockshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.mockshop.entity.Customer;
import project.mockshop.entity.Order;
import project.mockshop.entity.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {
    List<Order> findAllByCustomer(Customer customer);

    List<Order> findAllByCustomerName(String customerName);

    List<Order> findAllByCustomerLoginId(String customerLoginId);

    List<Order> findAllByCustomerPhoneNumber(String customerPhoneNumber);

    Order findByOrderNumber(String orderNumber);

    List<Order> findAllByStatus(OrderStatus status);

    List<Order> findAllByOrderDateBetween(LocalDateTime from, LocalDateTime to);
}
