package project.mockshop.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import project.mockshop.entity.Item;

import java.util.List;

import static project.mockshop.entity.QItem.item;

public class ItemRepositoryImpl implements ItemRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public ItemRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<Item> findAllByQuantity(int min, int max) {
        return queryFactory
                .selectFrom(item)
                .where(item.quantity.between(min, max))
                .fetch();
    }

    public List<Item> findAllByPrice(int min, int max) {
        return queryFactory
                .selectFrom(item)
                .where(item.price.between(min, max))
                .fetch();
    }

    public List<Item> findAllByDiscount() {
        return queryFactory
                .selectFrom(item)
                .where(item.percentOff.gt(0))
                .fetch();
    }
}
