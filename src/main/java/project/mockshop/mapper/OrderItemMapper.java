package project.mockshop.mapper;

import project.mockshop.dto.OrderItemDto;
import project.mockshop.entity.OrderItem;

public class OrderItemMapper {
    public static OrderItemDto toDto(OrderItem orderItem) {
//        System.out.println("orderItem.getOrder() = " + orderItem.getOrder());
        System.out.println("orderItem.getId() = " + orderItem.getId());
        
        return OrderItemDto.builder()
                .id(orderItem.getId())
//                .order(OrderMapper.toDto(orderItem.getOrder()))
                .item(ItemMapper.toDto(orderItem.getItem()))
                .orderPrice(orderItem.getOrderPrice())
                .count(orderItem.getCount())
                .build();
    }
}
