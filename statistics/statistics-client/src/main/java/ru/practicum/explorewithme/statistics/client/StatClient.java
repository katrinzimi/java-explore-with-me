package ru.practicum.explorewithme.statistics.client;

import ru.practicum.explorewithme.statistics.dto.EndpointHitDto;
import ru.practicum.explorewithme.statistics.dto.StatsRequestDto;
import ru.practicum.explorewithme.statistics.dto.ViewStatsDto;

import java.util.List;

public interface StatClient {
    List<ViewStatsDto> stats(StatsRequestDto requestDto);

    void hit(EndpointHitDto endpointHit);
}
