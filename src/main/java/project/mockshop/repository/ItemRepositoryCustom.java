package project.mockshop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import project.mockshop.dto.ItemSearchCondition;
import project.mockshop.dto.ItemThumbDto;
import project.mockshop.entity.Item;

import java.util.List;

public interface ItemRepositoryCustom {
    List<Item> findAllByQuantity(int min, int max);
    List<Item> findAllByPrice(int min, int max);
    List<Item> findAllByDiscount();
    List<Item> findBestFiveThisWeek();
    Page<ItemThumbDto> search(ItemSearchCondition searchCondition, Pageable pageable);

    Slice<ItemThumbDto> searchSlice(ItemSearchCondition searchCondition, Pageable pageable);

    Slice<ItemThumbDto> searchSliceSortBySales(ItemSearchCondition searchCondition, Pageable pageable);
}
