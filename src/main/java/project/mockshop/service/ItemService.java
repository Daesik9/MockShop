package project.mockshop.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.mockshop.dto.ItemDto;
import project.mockshop.entity.Category;
import project.mockshop.entity.Item;
import project.mockshop.mapper.ItemMapper;
import project.mockshop.repository.ItemRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    public Long createItem(ItemDto  itemDto, Long merchantId) {
        Item item = ItemMapper.toEntity(itemDto);
        itemRepository.save(item);

        return item.getId();
    }

    @PostConstruct
    public void init() {
        createItem(ItemDto.builder().name("사과").price(1000).quantity(100).build(), 1L);
    }

    public ItemDto findById(Long id) {
        Optional<Item> itemOptional = itemRepository.findById(id);
        if (itemOptional.isEmpty()) {
            throw new NullPointerException("해당 상품이 없습니다.");
        }

        return ItemMapper.toDto(itemOptional.get());
    }

    public List<ItemDto> findItemsByNameLike(String name) {
        return itemRepository.findAllByNameLike("%" + name + "%").stream().map(ItemMapper::toDto).toList();
    }

    public List<ItemDto> findAllByCategory(Category category) {
        return itemRepository.findAllByCategory(category).stream().map(ItemMapper::toDto).toList();
    }

    public List<ItemDto> findItemsByMerchantName(String merchantName) {
        return itemRepository.findAllByMerchantName(merchantName).stream().map(ItemMapper::toDto).toList();
    }

    public List<ItemDto> findItemsByQuantity(int min, int max) {
        if (min < 0 || max < 0 || min > max) {
            throw new IllegalArgumentException("입력 값을 확인해주세요.");
        }
        return itemRepository.findAllByQuantity(min, max).stream().map(ItemMapper::toDto).toList();
    }

    public List<ItemDto> findItemsByPrice(int min, int max) {
        if (min < 0 || max < 0 || min > max) {
            throw new IllegalArgumentException("입력 값을 확인해주세요.");
        }

        return itemRepository.findAllByPrice(min, max).stream().map(ItemMapper::toDto).toList();
    }

    public List<ItemDto> findDiscountItems() {
        return itemRepository.findAllByDiscount().stream().map(ItemMapper::toDto).toList();
    }
}
