package ru.practicum.explorewithme.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class StatsRequestDto {
    private LocalDateTime start;
    private LocalDateTime end;
    private List<String> uri;
    private Boolean unique;
}
