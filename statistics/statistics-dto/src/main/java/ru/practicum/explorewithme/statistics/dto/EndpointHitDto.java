package ru.practicum.explorewithme.statistics.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class EndpointHitDto {
    private final String app;
    private final String uri;
    private final String ip;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime timestamp;
}
