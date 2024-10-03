package ru.practicum.explorewithme.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ViewStatsDto {
    private final String app;
    private final String uri;
    private final long hits;
}
