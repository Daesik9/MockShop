package project.mockshop.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import project.mockshop.entity.*;

public class CartRepositoryImpl implements CartRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public CartRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public Cart findCartWithItems(Long customerId) {
        QCart cart = QCart.cart;
        QCartItem cartItem = QCartItem.cartItem;
        QItem item = QItem.item;

        return queryFactory.selectFrom(cart)
                .leftJoin(cart.cartItems, cartItem).fetchJoin()
                .leftJoin(cartItem.item, item).fetchJoin()
                .where(cart.customer.id.eq(customerId))
                .fetchOne();
    }
}
