package ru.practicum.explorewithme.statistics.dto;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class EndpointHitDto {
    private final String app;
    private final String uri;
    private final String ip;
    private final LocalDateTime timestamp;
}
