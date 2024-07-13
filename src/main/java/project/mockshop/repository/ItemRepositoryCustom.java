package project.mockshop.repository;

import project.mockshop.dto.ItemDto;
import project.mockshop.entity.Item;

import java.util.List;

public interface ItemRepositoryCustom {
    List<Item> findAllByQuantity(int min, int max);
    List<Item> findAllByPrice(int min, int max);
    List<Item> findAllByDiscount();
    List<Item> findBestFiveThisWeek();
}
