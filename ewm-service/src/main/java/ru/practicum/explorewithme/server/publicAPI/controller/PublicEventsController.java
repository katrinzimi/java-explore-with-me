package ru.practicum.explorewithme.server.publicAPI.controller;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.server.dto.event.EventFullDto;
import ru.practicum.explorewithme.server.dto.event.EventShortDto;
import ru.practicum.explorewithme.server.publicAPI.dto.RequestParamEvent;
import ru.practicum.explorewithme.server.publicAPI.dto.RequestParamEventSort;
import ru.practicum.explorewithme.server.publicAPI.servise.event.PublicEventsService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/events")
@Validated
public class PublicEventsController {

    public final PublicEventsService eventsService;

    @GetMapping
    public List<EventShortDto> getAll(@RequestParam(required = false) String text,
                                      @RequestParam(required = false) List<Long> categories,
                                      @RequestParam(required = false) Boolean paid,
                                      @RequestParam(required = false) LocalDateTime rangeStart,
                                      @RequestParam(required = false) LocalDateTime rangeEnd,
                                      @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                      @RequestParam(defaultValue = "EVENT_DATE") RequestParamEventSort sort,
                                      @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                      @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("");
        RequestParamEvent param = RequestParamEvent
                .builder()
                .text(text)
                .categories(categories)
                .paid(paid)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .onlyAvailable(onlyAvailable)
                .sort(sort)
                .from(from)
                .size(size)
                .build();
        return eventsService.getAll(param);
    }

    @GetMapping("/{id}")
    public EventFullDto get(@PathVariable Long id) {
        log.info("");
        return eventsService.get(id);
    }
}