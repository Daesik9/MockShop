package project.mockshop.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class OrderDto {
    private Long id;
    private String orderNumber;
    private CustomerDto customer;
    private List<OrderItemDto> orderItems;
    private String status;
    private LocalDateTime orderDate;

        //==조회 로직==//
        /** 전체 결제금액(수량*상품금액(할인된) */
//        public int getTotalPrice() {
//            int totalPrice = 0;
//            for (OrderItemDto orderItem : orderItems) {
//                totalPrice += orderItem.getTotalPrice();
//            }
//            return totalPrice;
//        }

}
