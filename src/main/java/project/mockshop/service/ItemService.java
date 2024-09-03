package project.mockshop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.mockshop.dto.ItemCreationDto;
import project.mockshop.dto.ItemDto;
import project.mockshop.dto.ItemSearchCondition;
import project.mockshop.entity.*;
import project.mockshop.mapper.ItemMapper;
import project.mockshop.repository.ItemRepository;
import project.mockshop.repository.MemberRepository;
import project.mockshop.repository.MerchantRepository;
import project.mockshop.util.FileStore;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ItemService {
    private final ItemRepository itemRepository;
    private final MerchantRepository merchantRepository;
    private final FileStore fileStore;

    @Transactional
    public Long createItem(ItemCreationDto itemCreationDto) throws IOException {
        Item item = storeItemFile(itemCreationDto);
        itemRepository.save(item);

        return item.getId();
    }

    /**
     * ItemCreationDto에서 Item으로 변환 및 이미지들 저장.
     * @param itemCreationDto
     * @return
     * @throws IOException
     */
    private Item storeItemFile(ItemCreationDto itemCreationDto) throws IOException {
        Item item = ItemMapper.toEntity(itemCreationDto);

        if (itemCreationDto.getThumbnail() != null) {
            UploadFile thumbnail = fileStore.createUploadFile(itemCreationDto.getThumbnail());
            fileStore.storeFile(itemCreationDto.getThumbnail(), thumbnail);
            item.changeThumbnail(thumbnail);
        }
        if (itemCreationDto.getDescriptionImg1() != null) {
            UploadFile descriptionImg1 = fileStore.createUploadFile(itemCreationDto.getDescriptionImg1());
            fileStore.storeFile(itemCreationDto.getDescriptionImg1(), descriptionImg1);
            item.changeDescriptionImg1(descriptionImg1);
        }
        if (itemCreationDto.getDescriptionImg2() != null) {
            UploadFile descriptionImg2 = fileStore.createUploadFile(itemCreationDto.getDescriptionImg2());
            fileStore.storeFile(itemCreationDto.getDescriptionImg2(), descriptionImg2);
            item.changeDescriptionImg2(descriptionImg2);
        }
        if (itemCreationDto.getDescriptionImg3() != null) {
            UploadFile descriptionImg3 = fileStore.createUploadFile(itemCreationDto.getDescriptionImg3());
            fileStore.storeFile(itemCreationDto.getDescriptionImg3(), descriptionImg3);
            item.changeDescriptionImg3(descriptionImg3);
        }
        return item;
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

    public Page<ItemDto> findItemsByNameLike(String name, Pageable pageable) {
        return itemRepository.findAllByNameLike("%" + name + "%", pageable).map(ItemMapper::toDto);
    }

    public List<ItemDto> findAllByCategory(Category category) {
        return itemRepository.findAllByCategory(category).stream().map(ItemMapper::toDto).toList();
    }

    public List<ItemDto> findItemsByMerchantId(Long merchantId) {
        Merchant merchant = merchantRepository.findById(merchantId).orElseThrow();
        return itemRepository.findAllByMerchant(merchant).stream().map(ItemMapper::toDto).toList();
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

    public List<ItemDto> findBestFiveThisWeek() {
        return itemRepository.findBestFiveThisWeek().stream().map(ItemMapper::toDto).toList();
    }

//    public Page<ItemDto> search(ItemSearchCondition searchCond, Pageable pageable) {
//        log.info("itemService search " + searchCond + " " + pageable);
//        return itemRepository.search(searchCond, pageable).map(ItemMapper::toDto);
//    }

    public Page<ItemDto> search(ItemSearchCondition searchCond, Pageable pageable) {
        log.info("itemService search " + searchCond + " " + pageable);
        return itemRepository.search(searchCond, pageable).map(ItemMapper::toDto);
    }
}
