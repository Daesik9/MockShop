package project.mockshop.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString(of = {"itemNameLike", "priceGoe", "priceLoe", "isOnSale", "sortBy"})
public class ItemSearchCondition {
    private String itemNameLike;
    private Integer priceGoe;
    private Integer priceLoe;
    private Boolean isOnSale;
    private String sortBy;
}
