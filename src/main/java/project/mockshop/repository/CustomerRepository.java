package project.mockshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.mockshop.entity.Customer;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByLoginId(String loginId);

    Optional<Customer> findLoginIdByPhoneNumber(String phoneNumber);
}
