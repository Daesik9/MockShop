package project.mockshop.repository;

import project.mockshop.entity.Cart;


public interface CartRepositoryCustom {
    Cart findCartWithItems(Long customerId);
}
