package ru.practicum.explorewithme.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.server.model.Request;
import ru.practicum.explorewithme.server.model.enums.RequestState;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByEventIdAndStatus(Long eventId, RequestState requestState);

    List<Request> findAllByRequesterId(Long userId);

    Request findByIdAndRequesterId(Long requestsId, Long userId);

    List<Request> findAllByEventId(Long eventId);

    boolean existsByRequesterIdAndEventId(Long userId, Long eventId);
}
