package project.mockshop.mapper;

import project.mockshop.dto.OrderItemDto;
import project.mockshop.entity.OrderItem;
import project.mockshop.entity.UploadFile;

public class OrderItemMapper {
    public static OrderItemDto toDto(OrderItem orderItem) {
//        System.out.println("orderItem.getOrder() = " + orderItem.getOrder());
        System.out.println("orderItem.getId() = " + orderItem.getId());


        UploadFile thumbnail = orderItem.getItem().getThumbnail();

        return OrderItemDto.builder()
                .id(orderItem.getId())
//                .order(OrderMapper.toDto(orderItem.getOrder()))
//                .item(ItemMapper.toDto(orderItem.getItem()))
                .thumbnail(thumbnail == null ? null : thumbnail.getStoreFileName())
                .itemId(orderItem.getItem().getId())
                .name(orderItem.getItem().getName())
                .orderPrice(orderItem.getOrderPrice())
                .count(orderItem.getCount())
                .build();
    }
}
