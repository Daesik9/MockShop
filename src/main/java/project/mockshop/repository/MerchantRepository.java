package project.mockshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.mockshop.entity.Merchant;

public interface MerchantRepository extends JpaRepository<Merchant, Long> {

}
