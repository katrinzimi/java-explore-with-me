package ru.practicum.explorewithme.server.event.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.server.event.dto.*;
import ru.practicum.explorewithme.server.event.dto.comment.CommentDto;
import ru.practicum.explorewithme.server.event.dto.comment.CommentNewDto;
import ru.practicum.explorewithme.server.event.dto.comment.CommentUpdateDto;
import ru.practicum.explorewithme.server.event.service.CommentService;
import ru.practicum.explorewithme.server.event.service.EventService;
import ru.practicum.explorewithme.statistics.client.StatClient;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/events")
@Validated
public class PublicEventsController {

    private final EventService eventsService;
    private final CommentService commentService;
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
                .request(RequestContext.builder().uri(request.getRequestURI()).ip(request.getRemoteAddr()).build())
                .build();
        List<EventShortDto> result = eventsService.getAllShortEvents(param);
        return result;
    }

    @GetMapping("/{id}")
    public EventFullDto get(@PathVariable Long id, HttpServletRequest request) {
        log.info("Получение события по id {}", id);
        RequestContext requestContext = RequestContext.builder().uri(request.getRequestURI()).ip(request.getRemoteAddr()).build();
        return eventsService.get(id, requestContext);
    }

    @PostMapping("/{eventId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto save(@PathVariable Long eventId, @RequestBody CommentNewDto dto) {
        log.info(" {}", eventId);
        return commentService.save(eventId, dto);
    }

    @PatchMapping("/{eventId}/comments/{commentId}")
    public CommentDto update(@PathVariable Long eventId, @PathVariable Long commentId,
                             @RequestBody CommentUpdateDto dto) {
        log.info(" {}", eventId);
        return commentService.update(eventId, commentId, dto);
    }

    @GetMapping("/{eventId}/comments")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> getAllComments(@PathVariable Long eventId,
                                           @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                           @RequestParam(defaultValue = "10") @Positive int size) {
        log.info(" {}", eventId);
        PageRequest request = PageRequest.of(from / size, size,
                Sort.by(Sort.Direction.ASC, "id"));
        return commentService.getAllComments(eventId, request);
    }
}