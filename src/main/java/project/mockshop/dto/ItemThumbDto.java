package project.mockshop.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ItemThumbDto {
    private Long id;
    private String name;
    private String thumbnail;
    private int price;
    private double percentOff;

    @QueryProjection
    public ItemThumbDto(Long id, String name, String thumbnail, int price, double percentOff) {
        this.id = id;
        this.name = name;
        this.thumbnail = thumbnail;
        this.price = price;
        this.percentOff = percentOff;
    }
}
