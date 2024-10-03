package ru.practicum.explorewithme.statistics.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.statistics.dto.ViewStatsDto;
import ru.practicum.explorewithme.statistics.server.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointHitRepository extends JpaRepository<EndpointHit, Long> {
    @Query("""
            SELECT new ru.practicum.explorewithme.statistics.dto.ViewStatsDto(h.app, h.uri, count(h.ip))
            FROM EndpointHit h
            WHERE (h.timestamp BETWEEN :start AND :end) AND (h.uri IN :uri)
            GROUP BY h.app, h.uri
            ORDER BY count(h.ip) DESC
            """)
    List<ViewStatsDto> getStatistics(LocalDateTime start, LocalDateTime end, List<String> uri);

    @Query("""
            SELECT new ru.practicum.explorewithme.statistics.dto.ViewStatsDto(h.app, h.uri, count(h.ip))
            FROM EndpointHit h
            WHERE (h.timestamp BETWEEN :start AND :end)
            GROUP BY h.app, h.uri
            ORDER BY count(h.ip) DESC
            """)
    List<ViewStatsDto> getStatistics(LocalDateTime start, LocalDateTime end);

    @Query("""
            SELECT new ru.practicum.explorewithme.statistics.dto.ViewStatsDto(h.app, h.uri, count(distinct h.ip))
            FROM EndpointHit h
            WHERE (h.timestamp BETWEEN :start AND :end) AND (h.uri IN :uri)
            GROUP BY h.app, h.uri
            ORDER BY count(distinct h.ip) DESC
            """)
    List<ViewStatsDto> getUniqueStatistics(LocalDateTime start, LocalDateTime end, List<String> uri);

    @Query("""
            SELECT new ru.practicum.explorewithme.statistics.dto.ViewStatsDto(h.app, h.uri, count(distinct h.ip))
            FROM EndpointHit h
            WHERE (h.timestamp BETWEEN :start AND :end)
            GROUP BY h.app, h.uri
            ORDER BY count(distinct h.ip) DESC
            """)
    List<ViewStatsDto> getUniqueStatistics(LocalDateTime start, LocalDateTime end);
}
