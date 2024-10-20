package ru.practicum.explorewithme.server.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.explorewithme.server.dto.event.EventShortDto;
import ru.practicum.explorewithme.server.model.Event;
import ru.practicum.explorewithme.server.publicAPI.dto.RequestParamEvent;

import java.util.List;


public interface EventRepository extends JpaRepository<Event, Long>, EventCriteriaRepository {
    @Query("""
            SELECT new ru.practicum.explorewithme.server.dto.event.EventShortDto
            (e.id,
            e.title,
            e.annotation,
            new ru.practicum.explorewithme.server.dto.category.CategoryDto(e.category.id,e.category.name),
            e.confirmedRequests,
            e.eventDate,
            new ru.practicum.explorewithme.server.dto.user.UserShortDto(e.initiator.id,e.initiator.name),
            e.paid,
            e.views)
            FROM Event e
            WHERE (e.annotation like '%:#{#request.text}%')
            """)
    List<EventShortDto> getAllShortEvents(@Param("request") RequestParamEvent paramEvent);

    List<Event> findAllByInitiatorId(Long userId, Pageable request);

    Event findByIdAndInitiatorId(Long eventId, Long userId);

    boolean existsByIdAndInitiatorId(Long eventId, Long userId);
}
