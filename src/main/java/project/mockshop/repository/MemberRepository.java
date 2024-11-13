package project.mockshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.mockshop.dto.LoginDto;
import project.mockshop.entity.Customer;
import project.mockshop.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    //    Optional<Customer> findByLoginId(String loginId);
    Optional<Member> findByLoginId(String loginId);

    @Query("SELECT new project.mockshop.dto.LoginDto(m.id, m.password, m.role) FROM Member m WHERE m.loginId = :loginId")
    Optional<LoginDto> findLoginDtoByLoginId(String loginId);

    Optional<Customer> findLoginIdByPhoneNumber(String phoneNumber);

    Optional<Member> findByEmail(String email);
}