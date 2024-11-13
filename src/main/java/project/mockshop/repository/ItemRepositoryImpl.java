package project.mockshop.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import project.mockshop.dto.ItemSearchCondition;
import project.mockshop.entity.Item;
import project.mockshop.entity.QCategory;
import project.mockshop.entity.QOrder;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import static project.mockshop.entity.QCategory.category;
import static project.mockshop.entity.QItem.item;
import static project.mockshop.entity.QOrderItem.orderItem;

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

    @Override
    public Page<Item> search(ItemSearchCondition searchCondition, Pageable pageable) {
        ///TODO: 필터: 금액대, 할인여부, 정렬: 낮은가격순, 높은가격순, 판매량순, 최신순
        List<Item> content = queryFactory
                .selectFrom(item)
                .leftJoin(orderItem).on(item.id.eq(orderItem.item.id))
                .where(itemNameLike(searchCondition.getItemNameLike()),
                        priceGoe(searchCondition.getPriceGoe()),
                        priceLoe(searchCondition.getPriceLoe()),
                        isOnSale(searchCondition.getIsOnSale()))
                .groupBy(item.id)
                .orderBy(getOrderSpecifier(searchCondition.getSortBy()), item.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory
                .select(item.count())
                .from(item)
                .leftJoin(orderItem).on(item.id.eq(orderItem.item.id))
                .where(itemNameLike(searchCondition.getItemNameLike()),
                        priceGoe(searchCondition.getPriceGoe()),
                        priceLoe(searchCondition.getPriceLoe()),
                        isOnSale(searchCondition.getIsOnSale()))
                .fetchOne();

//        return new PagedModel<>(new PageImpl<>(content, pageable, count));
        return new PageImpl<>(content, pageable, count);
    }

    private OrderSpecifier<?> getOrderSpecifier(String sortBy) {
        //TODO: null은 들어갈 수 없음!! -> 기본 정렬 방법을 넣을건지, null말고 다른 방법이 잇는지 찾아보기.
        //Array로 보내면 되는데, 그냥 기본 정렬을 넣는게 나을듯
        if (sortBy == null) return item.id.asc();
        return switch (sortBy) {
            case "lowPrice" -> item.price.asc();
            case "highPrice" -> item.price.desc();
            case "salesVolume" -> orderItem.count.sum().coalesce(0).desc();
//            case "latest" -> item.regDate.desc();
            default -> item.id.asc();
        };
    }

    private BooleanExpression itemNameLike(String itemName) {
        return itemName == null ? null : item.name.like("%" + itemName + "%");
    }

    private BooleanExpression priceGoe(Integer priceGoe) {
        return priceGoe == null ? null : item.price.goe(priceGoe);
    }

    private BooleanExpression priceLoe(Integer priceLoe) {
        return priceLoe == null ? null : item.price.loe(priceLoe);
    }

    private BooleanExpression isOnSale(Boolean isOnSale) {
        return (isOnSale == null || !isOnSale) ? null : item.percentOff.gt(0);
    }
}
