package project.mockshop.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import project.mockshop.entity.Item;
import project.mockshop.entity.QOrder;
import project.mockshop.entity.QOrderItem;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
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

    @Override
    public List<Item> findBestFiveThisWeek() {
        QOrder order = QOrder.order;
        QOrderItem orderItem = QOrderItem.orderItem;

        LocalDateTime now = LocalDateTime.now();
        LocalDate today = now.toLocalDate();
        LocalDate monday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDateTime mondayMidnight = monday.atStartOfDay();

        return queryFactory
                .select(item)
                .from(orderItem)
                .leftJoin(orderItem.item, item)
                .leftJoin(orderItem.order, order)
                .where(order.orderDate.between(mondayMidnight, now))
                .groupBy(item.id)
                .orderBy(orderItem.count.sum().desc())
                .limit(5)
                .fetch();
    }
}
