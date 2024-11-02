package ru.practicum.explorewithme.server.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.server.event.dto.ParticipationRequestDto;
import ru.practicum.explorewithme.server.request.service.PrivateRequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/users/{userId}/requests")
public class PrivateRequestController {

    private final PrivateRequestService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getRequests(@PathVariable Long userId) {
        log.info("Получение информации о заявках текущего пользователя на участие в чужих событиях");
        return service.getRequests(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto create(@PathVariable Long userId,
                                          @RequestParam(required = false) Long eventId) {
        log.info("Добавление запроса от текущего пользователя на участие в событии");
        return service.create(userId, eventId);
    }

    @PatchMapping("/{requestsId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public ParticipationRequestDto update(@PathVariable Long userId, @PathVariable Long requestsId) {
        log.info("Отмена своего запроса на участие в событии");
        return service.update(userId, requestsId);
    }


}