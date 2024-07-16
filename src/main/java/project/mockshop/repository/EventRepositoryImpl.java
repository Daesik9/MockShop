package project.mockshop.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import project.mockshop.entity.Event;
import project.mockshop.entity.QEvent;

import java.time.LocalDateTime;
import java.util.List;

import static project.mockshop.entity.QEvent.*;

public class EventRepositoryImpl implements EventRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public EventRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Event> findAllByDateTime(LocalDateTime dateTime) {

        return queryFactory.selectFrom(event)
                .where(event.startDate.before(dateTime).and(event.endDate.after(dateTime)))
                .fetch();
    }
}
