package project.mockshop.repository;

import project.mockshop.entity.Merchant;
import project.mockshop.entity.Order;

import java.util.List;

public interface OrderRepositoryCustom {
    List<Order> findAllByMerchantId(Long merchantId);
    List<Order> findAllByMerchantName(String merchantName);
    List<Order> findAllByItemName(String itemName);
}
