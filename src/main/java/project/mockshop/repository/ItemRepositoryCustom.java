package project.mockshop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.mockshop.dto.ItemSearchCondition;
import project.mockshop.entity.Item;

import java.util.List;

public interface ItemRepositoryCustom {
    List<Item> findAllByQuantity(int min, int max);
    List<Item> findAllByPrice(int min, int max);
    List<Item> findAllByDiscount();
    List<Item> findBestFiveThisWeek();
    Page<Item> search(ItemSearchCondition searchCondition, Pageable pageable);
}
