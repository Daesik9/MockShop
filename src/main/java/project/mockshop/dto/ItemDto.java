package project.mockshop.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;
import project.mockshop.entity.Merchant;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"name", "price", "quantity"})
public class ItemDto {
    private Long id;
    private String name;
    private CategoryDto categoryDto;
    private String thumbnail;
    private int price;
    private int quantity;
    private String descriptionImg1;
    private String descriptionImg2;
    private String descriptionImg3;
    private double percentOff;
    //        private Long merchantId;
    private MerchantDto merchantDto;

    @QueryProjection
    public ItemDto(Long id, String name, CategoryDto categoryDto, String thumbnail, int price,
                   int quantity, String descriptionImg1, String descriptionImg2, String descriptionImg3,
                   double percentOff, MerchantDto merchantDto) {
        this.id = id;
        this.name = name;
        this.categoryDto = categoryDto;
        this.thumbnail = thumbnail;
        this.price = price;
        this.quantity = quantity;
        this.descriptionImg1 = descriptionImg1;
        this.descriptionImg2 = descriptionImg2;
        this.descriptionImg3 = descriptionImg3;
        this.percentOff = percentOff;
        this.merchantDto = merchantDto;
    }
}
