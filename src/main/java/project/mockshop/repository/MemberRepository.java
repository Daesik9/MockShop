package project.mockshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.mockshop.entity.Customer;
import project.mockshop.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    //    Optional<Customer> findByLoginId(String loginId);
    Optional<Member> findByLoginId(String loginId);

    Optional<Customer> findLoginIdByPhoneNumber(String phoneNumber);
}