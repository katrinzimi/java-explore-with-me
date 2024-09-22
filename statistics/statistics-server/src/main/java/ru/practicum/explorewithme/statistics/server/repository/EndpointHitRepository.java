package ru.practicum.explorewithme.statistics.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.statistics.server.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointHitRepository extends JpaRepository<EndpointHit, Long> {

    List<EndpointHit> findAllByTimestampBetweenAndUriIn(LocalDateTime start, LocalDateTime end, List<String> uri);

    List<EndpointHit> findAllByTimestampBetween(LocalDateTime start, LocalDateTime end);
}
