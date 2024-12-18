package ru.practicum.explorewithme.statistics.server.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.statistics.dto.EndpointHitDto;
import ru.practicum.explorewithme.statistics.dto.StatsRequestDto;
import ru.practicum.explorewithme.statistics.dto.ViewStatsDto;
import ru.practicum.explorewithme.statistics.server.exception.ValidationException;
import ru.practicum.explorewithme.statistics.server.model.EndpointHitMapper;
import ru.practicum.explorewithme.statistics.server.repository.EndpointHitRepository;

import java.util.List;

import static java.time.temporal.ChronoUnit.SECONDS;

@Service
@AllArgsConstructor
@Slf4j
public class EndpointHitServiceImpl implements EndpointHitService {
    private final EndpointHitRepository repository;

    @Override
    public void hit(EndpointHitDto endpointHit) {
        repository.save(EndpointHitMapper.toEndpointHit(endpointHit));
    }

    @Override
    public List<ViewStatsDto> getStats(StatsRequestDto requestDto) {
        if (requestDto.getEnd() == null || requestDto.getStart() == null) {
            throw new ValidationException("Отсутствуют даты начаа и конца");
        }

        requestDto.setStart(requestDto.getStart().truncatedTo(SECONDS));

        if (requestDto.getStart().isAfter(requestDto.getEnd())) {
            throw new ValidationException("Неверные даты");
        }
        log.info("requestDto: {}", requestDto);
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
