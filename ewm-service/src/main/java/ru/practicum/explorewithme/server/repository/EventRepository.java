package ru.practicum.explorewithme.server.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.server.model.Event;

import java.util.List;


public interface EventRepository extends JpaRepository<Event, Long>, EventCriteriaRepository {
    List<Event> findAllByInitiatorId(Long userId, Pageable request);

    Event findByIdAndInitiatorId(Long eventId, Long userId);

    boolean existsByIdAndInitiatorId(Long eventId, Long userId);

    List<Event> findAllByIdIn(List<Long> eventsId);

    List<Event> findAllByCategoryId(long catId);
}
