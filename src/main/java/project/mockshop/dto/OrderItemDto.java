package project.mockshop.dto;

import lombok.Builder;

@Builder
public class OrderItemDto {
    private Long id;
    private ItemDto item;
    private OrderDto order;
    private int orderPrice;
    private int count;
}
