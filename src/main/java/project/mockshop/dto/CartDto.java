package project.mockshop.dto;

import lombok.Builder;
import lombok.Getter;
import project.mockshop.entity.CartItem;
import project.mockshop.entity.Customer;

import java.util.List;

@Builder
@Getter
public class CartDto {
    private Long id;
    private Customer customer;
    private List<CartItem> cartItems;
}
