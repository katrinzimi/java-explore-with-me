package ru.practicum.explorewithme.server.event.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.server.event.dto.*;
import ru.practicum.explorewithme.server.event.service.CommentService;
import ru.practicum.explorewithme.server.event.service.EventService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/users/{userId}/events")
public class PrivateEventsController {

    public final EventService service;
    private final CommentService commentService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getAll(@PathVariable Long userId,
                                      @RequestParam(defaultValue = "0") Integer from,
                                      @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получение событий, добавленных текущим пользователем");
        return service.getAllShortEvents(userId, from, size);
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto get(@PathVariable Long userId,
                            @PathVariable Long eventId) {
        log.info("Получение полной информации о событии добавленном текущим пользователем");
        return service.get(userId, eventId);
    }

    @GetMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getRequests(@PathVariable Long userId,
                                                     @PathVariable Long eventId) {
        log.info("Получение полной информации о событии добавленном текущим пользователем");
        return service.getRequests(userId, eventId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto create(@PathVariable Long userId,
                               @RequestBody @Valid NewEventDto eventDto) {
        log.info("Добавление нового события");
        return service.create(userId, eventDto);
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto update(@PathVariable Long userId, @PathVariable Long eventId,
                               @RequestBody @Valid UpdateEventUserRequest eventDto) {
        log.info("Изменение события добавленного текущим пользователем");
        return service.update(userId, eventId, eventDto);
    }

    @PatchMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public EventRequestStatusUpdateResult updateRequestStatus(@PathVariable Long userId,
                                                              @PathVariable Long eventId,
                                                              @RequestBody EventRequestStatusUpdateRequest request) {
        log.info("Изменение статуса (подтверждена, отменена) заявок на участие в событии текущего пользователя");
        return service.updateRequestStatus(userId, eventId, request);
    }

}
