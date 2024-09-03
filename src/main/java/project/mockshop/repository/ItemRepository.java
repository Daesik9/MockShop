package project.mockshop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import project.mockshop.entity.Category;
import project.mockshop.entity.Item;
import project.mockshop.entity.Merchant;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {
    List<Item> findAllByNameLike(String name);

    Page<Item> findAllByNameLike(String name, Pageable pageable);

    List<Item> findAllByCategory(Category category);

    List<Item> findAllByMerchant(Merchant merchant);
}
