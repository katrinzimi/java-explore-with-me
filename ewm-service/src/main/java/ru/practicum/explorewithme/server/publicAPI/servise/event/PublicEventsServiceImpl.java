package ru.practicum.explorewithme.server.publicAPI.servise.event;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.server.adminAPI.exception.NotFoundException;
import ru.practicum.explorewithme.server.dto.event.EventFullDto;
import ru.practicum.explorewithme.server.dto.event.EventShortDto;
import ru.practicum.explorewithme.server.dto.mapper.EventMapper;
import ru.practicum.explorewithme.server.exception.ValidationException;
import ru.practicum.explorewithme.server.model.Event;
import ru.practicum.explorewithme.server.model.enums.EventState;
import ru.practicum.explorewithme.server.publicAPI.dto.RequestParamEvent;
import ru.practicum.explorewithme.server.repository.EventRepository;
import ru.practicum.explorewithme.statistics.client.StatClient;
import ru.practicum.explorewithme.statistics.dto.EndpointHitDto;
import ru.practicum.explorewithme.statistics.dto.StatsRequestDto;
import ru.practicum.explorewithme.statistics.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicEventsServiceImpl implements PublicEventsService {
    private final EventRepository repository;
    @Value("${ewm.service.name}")
    private String serviceName;
    private final StatClient statsClient;

    @Override
    public List<EventShortDto> getAll(RequestParamEvent param) {
        if (param.getRangeEnd() != null && param.getRangeStart() != null
                && param.getRangeEnd().isBefore(param.getRangeStart())) {
            throw new ValidationException("Даты указаны не верно");
        }
        List<Event> events = repository.findAllByCriteria(param).getContent();
        List<EventShortDto> result = events.stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());

        if (!result.isEmpty()) {
            LocalDateTime minEventDate = result.stream().min(Comparator.comparing(EventShortDto::getEventDate)).orElseThrow().getEventDate();
            Map<String, EventShortDto> uris = result.stream()
                    .map(e -> Pair.of("/events/" + e.getId(), e))
                    .collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
            List<ViewStatsDto> stats = statsClient.stats(StatsRequestDto.builder()
                    .start(minEventDate)
                    .end(LocalDateTime.now())
                    .unique(true)
                    .uri(uris.keySet().stream().toList())
                    .build());
            for (ViewStatsDto stat : stats) {
                EventShortDto eventShortDto = uris.get(stat.getUri());
                eventShortDto.setViews((int) stat.getHits());
            }
        }

        saveEndpointHit(param.getRequest());
        return result;
    }

    @Override
    public EventFullDto get(Long id, HttpServletRequest request) {
        Event event = repository.findById(id).orElseThrow();
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new NotFoundException("");
        }
        saveEndpointHit(request);
        List<ViewStatsDto> stats = statsClient.stats(StatsRequestDto.builder()
                .start(event.getCreatedOn())
                .end(LocalDateTime.now())
                .unique(true)
                .uri(List.of("/events/" + id))
                .build());
        EventFullDto result = EventMapper.toEventFullDto(event);
        if (!stats.isEmpty()) {
            result.setViews((int) stats.get(0).getHits());
        } else {
            System.out.println("lololo");
        }
        return result;
    }

    private void saveEndpointHit(HttpServletRequest request) {

        EndpointHitDto endpointHit = EndpointHitDto.builder()
                .ip(request.getRemoteAddr())
                .uri(request.getRequestURI())
                .app(serviceName)
                .timestamp(LocalDateTime.now())
                .build();
        statsClient.hit(endpointHit);
    }
}
