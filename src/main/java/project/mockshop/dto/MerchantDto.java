package project.mockshop.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MerchantDto {
    private Long id;
    private String name;
    private String storeName;
}
