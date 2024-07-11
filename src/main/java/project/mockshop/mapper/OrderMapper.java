package project.mockshop.mapper;

import project.mockshop.dto.OrderDto;
import project.mockshop.entity.Order;

public class OrderMapper {
    public static OrderDto toDto(Order order) {

        System.out.println("order.getId() = " + order.getId());
        
        return OrderDto.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .customerDto(CustomerMapper.toDto(order.getCustomer()))
                .orderItemDtos(order.getOrderItems().stream().map(OrderItemMapper::toDto).toList())
                .status(order.getStatus())
                .orderDate(order.getOrderDate())
                .paymentMethod(order.getPaymentMethod())
                .address(order.getAddress())
                .build();
    }
}
