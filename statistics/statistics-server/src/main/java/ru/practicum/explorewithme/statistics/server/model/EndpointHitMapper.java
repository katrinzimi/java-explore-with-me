package ru.practicum.explorewithme.statistics.server.model;

import ru.practicum.explorewithme.statistics.dto.EndpointHitDto;

public class EndpointHitMapper {
    public static EndpointHit toEndpointHit(EndpointHitDto dto) {
        return new EndpointHit(
                null,
                dto.getApp(),
                dto.getUri(),
                dto.getIp(),
                dto.getTimestamp()
        );
    }

    public static EndpointHitDto endpointHitDto(EndpointHit endpointHit) {
        if (endpointHit != null) {
            return new EndpointHitDto(
                    endpointHit.getApp(),
                    endpointHit.getUri(),
                    endpointHit.getIp(),
                    endpointHit.getTimestamp()
            );
        }
        return null;
    }
}
