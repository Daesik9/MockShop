package project.mockshop.mapper;

import project.mockshop.dto.ItemDto;
import project.mockshop.entity.Item;

public class ItemMapper {
    public static Item toEntity(ItemDto dto) {
        return Item.builder()
                .name(dto.getName())
                .category(dto.getCategory())
                .thumbnail(dto.getThumbnail())
                .price(dto.getPrice())
                .quantity(dto.getQuantity())
                .descriptionImg1(dto.getDescriptionImg1())
                .descriptionImg2(dto.getDescriptionImg2())
                .descriptionImg3(dto.getDescriptionImg3())
                .percentOff(dto.getPercentOff())
                .merchant(dto.getMerchant())
                .build();
    }

    public static ItemDto toDto(Item item) {
        return ItemDto.builder()
                .name(item.getName())
                .category(item.getCategory())
                .thumbnail(item.getThumbnail())
                .price(item.getPrice())
                .quantity(item.getQuantity())
                .descriptionImg1(item.getDescriptionImg1())
                .descriptionImg2(item.getDescriptionImg2())
                .descriptionImg3(item.getDescriptionImg3())
                .percentOff(item.getPercentOff())
                .merchant(item.getMerchant())
                .build();
    }
}
