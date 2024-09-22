package ru.practicum.explorewithme.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ViewStatsDto {
    final private String app;
    final private String uri;
    final private long hits;
}
