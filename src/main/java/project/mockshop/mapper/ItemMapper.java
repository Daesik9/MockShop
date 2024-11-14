package project.mockshop.mapper;

import project.mockshop.dto.CategoryDto;
import project.mockshop.dto.ItemCreationDto;
import project.mockshop.dto.ItemDto;
import project.mockshop.dto.ItemThumbDto;
import project.mockshop.entity.Item;
import project.mockshop.entity.UploadFile;

public class ItemMapper {
    public static Item toEntity(ItemDto dto) {
        return Item.builder()
                .id(dto.getId())
                .name(dto.getName())
                .category(CategoryMapper.toEntity(dto.getCategoryDto()))
                .thumbnail(UploadFile.builder().storeFileName(dto.getThumbnail()).build())
                .price(dto.getPrice())
                .quantity(dto.getQuantity())
                .descriptionImg1(UploadFile.builder().storeFileName(dto.getDescriptionImg1()).build())
                .descriptionImg2(UploadFile.builder().storeFileName(dto.getDescriptionImg2()).build())
                .descriptionImg3(UploadFile.builder().storeFileName(dto.getDescriptionImg3()).build())
                .percentOff(dto.getPercentOff())
                .merchant(MerchantMapper.toEntity(dto.getMerchantDto()))
                .build();
    }

    public static Item toEntity(ItemCreationDto dto) {
        return Item.builder()
                .id(dto.getId())
                .name(dto.getName())
                .category(dto.getCategory())
                .price(dto.getPrice())
                .quantity(dto.getQuantity())
                .percentOff(dto.getPercentOff())
                .merchant(dto.getMerchant())
                .build();
    }

    public static ItemDto toDto(Item item) {
        UploadFile thumbnail = item.getThumbnail();
        UploadFile descriptionImg1 = item.getDescriptionImg1();
        UploadFile descriptionImg2 = item.getDescriptionImg2();
        UploadFile descriptionImg3 = item.getDescriptionImg3();

        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .categoryDto(CategoryMapper.toDto(item.getCategory()))
                .thumbnail(thumbnail == null ? null : thumbnail.getStoreFileName())
                .price(item.getPrice())
                .quantity(item.getQuantity())
                .descriptionImg1(descriptionImg1 == null ? null : descriptionImg1.getStoreFileName())
                .descriptionImg2(descriptionImg2 == null ? null : descriptionImg2.getStoreFileName())
                .descriptionImg3(descriptionImg3 == null? null : descriptionImg3.getStoreFileName())
                .percentOff(item.getPercentOff())
                .merchantDto(MerchantMapper.toDto(item.getMerchant()))
                .build();
    }

    public static ItemThumbDto toThumbDto(Item item) {
        UploadFile thumbnail = item.getThumbnail();

        return ItemThumbDto.builder()
                .id(item.getId())
                .name(item.getName())
                .price(item.getPrice())
                .percentOff(item.getPercentOff())
                .thumbnail(thumbnail == null ? null : thumbnail.getStoreFileName())
                .build();
    }
}
