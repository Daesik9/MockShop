package project.mockshop.repository;

import project.mockshop.entity.Merchant;
import project.mockshop.entity.Order;

import java.util.List;

public interface OrderRepositoryCustom {
    List<Order> findAllByMerchant(Merchant merchant);
    List<Order> findAllByMerchantName(String merchantName);
    List<Order> findAllByItemName(String itemName);
}
