package ru.practicum.explorewithme.statistics.server.service;

import ru.practicum.explorewithme.statistics.dto.EndpointHitDto;
import ru.practicum.explorewithme.statistics.dto.StatsRequestDto;
import ru.practicum.explorewithme.statistics.dto.ViewStatsDto;

import java.util.List;

public interface EndpointHitService {

    void hit(EndpointHitDto endpointHit);

    List<ViewStatsDto> getStats(StatsRequestDto requestDto);
}
