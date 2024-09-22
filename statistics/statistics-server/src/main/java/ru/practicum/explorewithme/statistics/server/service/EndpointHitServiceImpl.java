package ru.practicum.explorewithme.statistics.server.service;

import lombok.AllArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.statistics.dto.EndpointHitDto;
import ru.practicum.explorewithme.statistics.dto.ViewStatsDto;
import ru.practicum.explorewithme.statistics.server.model.EndpointHit;
import ru.practicum.explorewithme.statistics.server.model.EndpointHitMapper;
import ru.practicum.explorewithme.statistics.server.repository.EndpointHitRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
        List<EndpointHit> endpointHits;

        if (uri.isEmpty()) {
            endpointHits = repository.findAllByTimestampBetween(start, end);
        } else {
            endpointHits = repository.findAllByTimestampBetweenAndUriIn(start, end, uri);
        }


        Map<Pair<String, String>, Collection<String>> groupData = new HashMap<>();
        for (EndpointHit endpointHit : endpointHits) {
            Pair<String, String> key = Pair.of(endpointHit.getApp(), endpointHit.getUri());
            if (unique) {
                groupData.computeIfAbsent(key, k -> new HashSet<>()).add(endpointHit.getIp());
            } else {
                groupData.computeIfAbsent(key, k -> new ArrayList<>()).add(endpointHit.getIp());
            }
        }

        return groupData.entrySet().stream()
                .map(e -> new ViewStatsDto(e.getKey().getFirst(), e.getKey().getSecond(), e.getValue().size()))
                .collect(Collectors.toList());

    }
}