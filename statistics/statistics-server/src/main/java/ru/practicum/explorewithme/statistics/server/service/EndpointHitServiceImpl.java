package ru.practicum.explorewithme.statistics.server.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.statistics.dto.EndpointHitDto;
import ru.practicum.explorewithme.statistics.dto.ViewStatsDto;
import ru.practicum.explorewithme.statistics.server.model.EndpointHitMapper;
import ru.practicum.explorewithme.statistics.server.repository.EndpointHitRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class EndpointHitServiceImpl implements EndpointHitService {
    private final EndpointHitRepository repository;

    @Override
    public void hit(EndpointHitDto endpointHit) {
        repository.save(EndpointHitMapper.toEndpointHit(endpointHit));
    }

    @Override
    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uri, Boolean unique) {
        if (unique) {
            if (uri.isEmpty()) {
                return repository.getUniqueStatistics(start, end);
            } else {
                return repository.getUniqueStatistics(start, end, uri);
            }
        } else {
            if (uri.isEmpty()) {
                return repository.getStatistics(start, end);
            } else {
                return repository.getStatistics(start, end, uri);
            }
        }
    }
}