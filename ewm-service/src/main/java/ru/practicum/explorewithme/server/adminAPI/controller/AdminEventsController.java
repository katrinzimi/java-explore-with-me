package ru.practicum.explorewithme.server.adminAPI.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.server.adminAPI.servise.event.EventService;
import ru.practicum.explorewithme.server.dto.event.EventFullDto;
import ru.practicum.explorewithme.server.dto.event.UpdateEventAdminRequest;
import ru.practicum.explorewithme.server.model.enums.EventState;
import ru.practicum.explorewithme.server.publicAPI.dto.RequestParamEvent;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin/events")
@Validated
public class AdminEventsController {

    private final EventService eventService;

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto update(@PathVariable Long eventId,
                               @RequestBody UpdateEventAdminRequest updateEvent) {
        log.info("Получен запрос на изменение события.");
        return eventService.update(eventId, updateEvent);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<EventFullDto> getAllEvents(@RequestParam(required = false) List<Long> users,
                                    @RequestParam(required = false) List<String> states,
                                    @RequestParam(required = false) List<Long> categories,
                                    @RequestParam(required = false)
                                    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                    LocalDateTime rangeStart,
                                    @RequestParam(required = false)
                                    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                    LocalDateTime rangeEnd,
                                    @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                    @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Получение списка событий.");
        RequestParamEvent param = RequestParamEvent.builder()
                .users(users)
                .states(states)
                .categories(categories)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .from(from)
                .size(size)
                .build();
        return eventService.getAll(param);
    }
}
