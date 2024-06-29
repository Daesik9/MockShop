package project.mockshop.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import project.mockshop.entity.*;

import java.util.List;

import static project.mockshop.entity.QItem.*;
import static project.mockshop.entity.QOrder.*;
import static project.mockshop.entity.QOrderItem.*;

public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public OrderRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Order> findAllByMerchant(Merchant merchant) {
        return queryFactory
                .selectFrom(order)
                .join(order.orderItems, orderItem)
                .join(orderItem.item, item)
                .where(item.merchant.eq(merchant))
                .distinct()
                .fetch();
    }

    @Override
    public List<Order> findAllByMerchantName(String merchantName) {
        return queryFactory
                .selectFrom(order)
                .join(order.orderItems, orderItem)
                .join(orderItem.item, item)
                .where(item.merchant.name.eq(merchantName))
                .distinct()
                .fetch();
    }

    @Override
    public List<Order> findAllByItemName(String itemName) {
        return queryFactory
                .selectFrom(order)
                .join(order.orderItems, orderItem)
                .join(orderItem.item, item)
                .where(item.name.eq(itemName))
                .distinct()
                .fetch();
    }

}
