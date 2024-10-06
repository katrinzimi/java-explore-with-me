package ru.practicum.explorewithme.server.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import ru.practicum.explorewithme.server.model.Category;
import ru.practicum.explorewithme.server.model.Event;
import ru.practicum.explorewithme.server.model.User;
import ru.practicum.explorewithme.server.publicAPI.dto.RequestParamEvent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
public class EventCriteriaRepositoryImpl implements EventCriteriaRepository {

    private final EntityManager entityManager;

    @Override
    public Page<Event> findAllByCriteria(RequestParamEvent request) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> query = builder.createQuery(Event.class);
        Root<Event> eventRoot = query.from(Event.class);

        List<Predicate> predicates = new ArrayList<>();

        if (request.getText() != null) {
            Predicate annotationPredicate = builder.like(
                    builder.lower(eventRoot.get("annotation")), "%" + request.getText() + "%"
            );
            predicates.add(annotationPredicate);
        }

        if (!request.getCategories().isEmpty()) {
            Join<Event, Category> categoryJoin = eventRoot.join("category");
            predicates.add(categoryJoin.get("id").in(request.getCategories()));
        }

        if (request.getPaid() != null) {
            predicates.add(builder.equal(eventRoot.get("paid"), request.getPaid()));
        }

        if (request.getRangeStart() != null || request.getRangeEnd() != null) {
            LocalDateTime rangeStart = Objects.requireNonNullElse(request.getRangeStart(), LocalDateTime.MIN);
            LocalDateTime rangeEnd = Objects.requireNonNullElse(request.getRangeEnd(), LocalDateTime.MAX);
            predicates.add(builder.between(eventRoot.get("eventDate"), rangeStart, rangeEnd));
        }

        if (Boolean.TRUE.equals(request.getOnlyAvailable())) {
            Predicate onlyAvailablePredicate = builder.or(
                    builder.isNull(eventRoot.get("participantLimit")),
                    builder.greaterThan(eventRoot.get("participantLimit"), eventRoot.get("confirmedRequests"))
            );
            predicates.add(onlyAvailablePredicate);
        }
        if (!request.getUsers().isEmpty()) {
            Join<Event, User> userJoin = eventRoot.join("initiator");
            predicates.add(userJoin.get("id").in(request.getUsers()));
        }
        if (!request.getStates().isEmpty()) {
            predicates.add(eventRoot.get("state").in(request.getStates()));
        }
        Predicate allPredicates = builder.and(predicates.toArray(new Predicate[0]));

        query.where(allPredicates);

        switch (request.getSort()) {
            case EVENT_DATE -> query.orderBy(builder.asc(eventRoot.get("eventDate")));
            case VIEWS -> query.orderBy(builder.asc(eventRoot.get("views")));
        }

        TypedQuery<Event> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult(request.getFrom());
        typedQuery.setMaxResults(request.getSize());
        List<Event> resultList = typedQuery.getResultList();

        return new PageImpl<>(resultList);

    }
}
