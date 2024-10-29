package ru.practicum.explorewithme.server.publicAPI.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.server.dto.event.EventFullDto;
import ru.practicum.explorewithme.server.dto.event.EventShortDto;
import ru.practicum.explorewithme.server.publicAPI.dto.RequestParamEvent;
import ru.practicum.explorewithme.server.publicAPI.dto.RequestParamEventSort;
import ru.practicum.explorewithme.server.publicAPI.servise.event.PublicEventsService;
import ru.practicum.explorewithme.statistics.client.StatClient;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/events")
@Validated
public class PublicEventsController {

    private final PublicEventsService eventsService;
    private final StatClient statClient;

    @GetMapping
    public List<EventShortDto> getAll(@RequestParam(required = false) String text,
                                      @RequestParam(required = false) List<Long> categories,
                                      @RequestParam(required = false) Boolean paid,
                                      @RequestParam(required = false)
                                      @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                      LocalDateTime rangeStart,
                                      @RequestParam(required = false)
                                      @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                      LocalDateTime rangeEnd,
                                      @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                      @RequestParam(defaultValue = "EVENT_DATE") RequestParamEventSort sort,
                                      @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                      @RequestParam(defaultValue = "10") @Positive int size,
                                      HttpServletRequest request) {
        log.info("Поучение списка событий");
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
                .request(request)
                .build();
        List<EventShortDto> result = eventsService.getAll(param);
        return result;
    }

    @GetMapping("/{id}")
    public EventFullDto get(@PathVariable Long id, HttpServletRequest request) {
        log.info("Получение события по id {}", id);
        return eventsService.get(id, request);
    }
}