package ru.practicum.explorewithme.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.server.dto.event.ParticipationRequestDto;
import ru.practicum.explorewithme.server.model.Request;
import ru.practicum.explorewithme.server.model.enums.EventState;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByEventAndState(Long eventId, EventState eventState);

    List<ParticipationRequestDto> findAllByRequesterId(Long userId);

    Request findByIdAndRequesterId(Long requestsId, Long userId);

    List<Request> findAllByEventId(Long eventId);
}
