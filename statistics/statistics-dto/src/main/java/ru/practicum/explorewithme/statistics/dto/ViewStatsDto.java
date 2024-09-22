package ru.practicum.explorewithme.statistics.dto;

import lombok.Data;

@Data
public class ViewStatsDto {
    final private String app;
    final private String uri;
    final private Integer hits;
}
