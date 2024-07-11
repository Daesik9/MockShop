package project.mockshop.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OrderItemDto {
    private Long id;
    private Long itemId;
    private String name;
    private String thumbnail;
//    private ItemDto item;
//    private OrderDto order;
    private int orderPrice;
    private int count;
}
