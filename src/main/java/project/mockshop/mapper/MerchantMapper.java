package project.mockshop.mapper;

import project.mockshop.dto.MerchantDto;
import project.mockshop.entity.Merchant;

public class MerchantMapper {
    public static MerchantDto toDto(Merchant merchant) {
        return MerchantDto.builder()
                .id(merchant.getId())
                .name(merchant.getName())
                .storeName(merchant.getStoreName())
                .build();
    }

    public static Merchant toEntity(MerchantDto merchantDto) {
        return Merchant.builder()
                .id(merchantDto.getId())
                .name(merchantDto.getName())
                .storeName(merchantDto.getStoreName())
                .build();
    }
}
