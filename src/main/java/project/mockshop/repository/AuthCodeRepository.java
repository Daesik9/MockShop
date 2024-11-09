package project.mockshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.mockshop.entity.AuthCode;

public interface AuthCodeRepository extends JpaRepository<AuthCode, Long> {
    AuthCode findByEmail(String email);
}
