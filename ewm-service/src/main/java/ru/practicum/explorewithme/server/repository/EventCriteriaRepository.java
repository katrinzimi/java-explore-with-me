package ru.practicum.explorewithme.server.repository;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.server.model.Event;
import ru.practicum.explorewithme.server.event.dto.RequestParamEvent;

@Repository
public interface EventCriteriaRepository {

    Page<Event> findAllByCriteria(RequestParamEvent request);
}
