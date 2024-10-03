package ru.practicum.explorewithme.statistics.server.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.statistics.dto.EndpointHitDto;
import ru.practicum.explorewithme.statistics.dto.StatsRequestDto;
import ru.practicum.explorewithme.statistics.dto.ViewStatsDto;
import ru.practicum.explorewithme.statistics.server.model.EndpointHitMapper;
import ru.practicum.explorewithme.statistics.server.repository.EndpointHitRepository;

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
    public List<ViewStatsDto> getStats(StatsRequestDto requestDto) {
        if (requestDto.getUnique()) {
            if (requestDto.getUri().isEmpty()) {
                return repository.getUniqueStatistics(requestDto.getStart(), requestDto.getEnd());
            }
            return repository.getUniqueStatistics(requestDto.getStart(), requestDto.getEnd(), requestDto.getUri());

        }
        if (requestDto.getUri().isEmpty()) {
            return repository.getStatistics(requestDto.getStart(), requestDto.getEnd());
        }
        return repository.getStatistics(requestDto.getStart(), requestDto.getEnd(), requestDto.getUri());

    }
}
