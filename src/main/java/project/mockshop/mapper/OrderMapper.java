package project.mockshop.mapper;

import project.mockshop.dto.OrderDto;
import project.mockshop.entity.Order;

public class OrderMapper {
    public static OrderDto toDto(Order order) {

        System.out.println("order.getId() = " + order.getId());
        
        return OrderDto.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .customer(CustomerMapper.toDto(order.getCustomer()))
                .orderItems(order.getOrderItems().stream().map(OrderItemMapper::toDto).toList())
                .status(order.getStatus())
                .orderDate(order.getOrderDate())
                .build();
    }
}
