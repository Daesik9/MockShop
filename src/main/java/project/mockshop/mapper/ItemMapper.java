package project.mockshop.mapper;

import project.mockshop.dto.ItemCreationDto;
import project.mockshop.dto.ItemDto;
import project.mockshop.entity.Item;
import project.mockshop.entity.UploadFile;
import project.mockshop.util.FileStore;

import java.io.IOException;

public class ItemMapper {
    public static Item toEntity(ItemDto dto) {
        return Item.builder()
                .id(dto.getId())
                .name(dto.getName())
                .category(dto.getCategory())
                .thumbnail(UploadFile.builder().storeFileName(dto.getThumbnail()).build())
                .price(dto.getPrice())
                .quantity(dto.getQuantity())
                .descriptionImg1(UploadFile.builder().storeFileName(dto.getDescriptionImg1()).build())
                .descriptionImg2(UploadFile.builder().storeFileName(dto.getDescriptionImg2()).build())
                .descriptionImg3(UploadFile.builder().storeFileName(dto.getDescriptionImg3()).build())
                .percentOff(dto.getPercentOff())
                .merchant(dto.getMerchant())
                .build();
    }

    public static Item toEntity(ItemCreationDto dto) {
        FileStore fileStore = new FileStore();
        UploadFile thumbnail = dto.getThumbnail() == null ? null : fileStore.createUploadFile(dto.getThumbnail());
        UploadFile descriptionImg1 = dto.getDescriptionImg1() == null ? null : fileStore.createUploadFile(dto.getDescriptionImg1());
        UploadFile descriptionImg2 = dto.getDescriptionImg2() == null ? null : fileStore.createUploadFile(dto.getDescriptionImg2());
        UploadFile descriptionImg3 = dto.getDescriptionImg3() == null ? null : fileStore.createUploadFile(dto.getDescriptionImg3());

        return Item.builder()
                .name(dto.getName())
                .category(dto.getCategory())
                .thumbnail(thumbnail)
                .price(dto.getPrice())
                .quantity(dto.getQuantity())
                .descriptionImg1(descriptionImg1)
                .descriptionImg2(descriptionImg2)
                .descriptionImg3(descriptionImg3)
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
                .category(item.getCategory())
                .thumbnail(thumbnail == null ? null : thumbnail.getStoreFileName())
                .price(item.getPrice())
                .quantity(item.getQuantity())
                .descriptionImg1(descriptionImg1 == null ? null : descriptionImg1.getStoreFileName())
                .descriptionImg2(descriptionImg2 == null ? null : descriptionImg2.getStoreFileName())
                .descriptionImg3(descriptionImg3 == null? null : descriptionImg3.getStoreFileName())
                .percentOff(item.getPercentOff())
                .merchant(item.getMerchant())
                .build();
    }

//    public static ItemDto toDto(ItemCreationDto creationDto) {
//        return ItemDto.builder()
//                .id(creationDto.getId())
//                .name(creationDto.getName())
//                .category(creationDto.getCategory())
//                .thumbnail(creationDto.getThumbnail())
//                .price(creationDto.getPrice())
//                .quantity(creationDto.getQuantity())
//                .descriptionImg1(creationDto.getDescriptionImg1().getStoreFileName())
//                .descriptionImg2(creationDto.getDescriptionImg2().getStoreFileName())
//                .descriptionImg3(creationDto.getDescriptionImg3().getStoreFileName())
//                .percentOff(creationDto.getPercentOff())
//                .merchant(creationDto.getMerchant())
//                .build();
//    }
}
