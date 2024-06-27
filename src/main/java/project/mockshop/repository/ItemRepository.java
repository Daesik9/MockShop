package project.mockshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.mockshop.entity.Category;
import project.mockshop.entity.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {
    List<Item> findAllByNameLike(String name);

    List<Item> findAllByCategory(Category category);

    List<Item> findAllByMerchantName(String merchantName);
}
