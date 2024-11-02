package ru.practicum.explorewithme.server.event.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestParamEvent {
    private String text;
    private List<Long> categories = List.of();
    private Boolean paid;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private Boolean onlyAvailable;
    private RequestParamEventSort sort;
    private int from;
    private int size;
    private List<Long> users = List.of();
    private List<String> states = List.of();
    private RequestContext request;
}
