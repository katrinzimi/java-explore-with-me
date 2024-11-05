package ru.practicum.explorewithme.server.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.server.event.dto.*;
import ru.practicum.explorewithme.server.exception.ConflictException;
import ru.practicum.explorewithme.server.exception.NotFoundException;
import ru.practicum.explorewithme.server.exception.ValidationException;
import ru.practicum.explorewithme.server.mapper.EventMapper;
import ru.practicum.explorewithme.server.mapper.RequestMapper;
import ru.practicum.explorewithme.server.model.Event;
import ru.practicum.explorewithme.server.model.Request;
import ru.practicum.explorewithme.server.model.User;
import ru.practicum.explorewithme.server.model.enums.EventState;
import ru.practicum.explorewithme.server.model.enums.RequestState;
import ru.practicum.explorewithme.server.model.enums.StateAction;
import ru.practicum.explorewithme.server.repository.EventRepository;
import ru.practicum.explorewithme.server.repository.RequestRepository;
import ru.practicum.explorewithme.server.repository.UserRepository;
import ru.practicum.explorewithme.statistics.client.StatClient;
import ru.practicum.explorewithme.statistics.dto.EndpointHitDto;
import ru.practicum.explorewithme.statistics.dto.StatsRequestDto;
import ru.practicum.explorewithme.statistics.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.practicum.explorewithme.server.model.enums.EventState.PUBLISHED;

@Service
@RequiredArgsConstructor

public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    @Value("${ewm.service.name}")
    private String serviceName;
    private final StatClient statsClient;

    @Override
    @Transactional
    public List<EventFullDto> getAllFullEvents(RequestParamEvent param) {
        List<Event> events = eventRepository.findAllByCriteria(param).getContent();
        return events.stream()
                .map(event -> EventMapper.toEventFullDto(event,
                        requestRepository.countByEventIdAndStatus(event.getId(), RequestState.CONFIRMED)))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<EventShortDto> getAllShortEvents(RequestParamEvent param) {
        if (param.getRangeEnd() != null && param.getRangeStart() != null
                && param.getRangeEnd().isBefore(param.getRangeStart())) {
            throw new ValidationException("Даты указаны не верно");
        }
        List<Event> events = eventRepository.findAllByCriteria(param).getContent();
        List<EventShortDto> result = EventMapper.toEventShortDtoList(events);
        if (!result.isEmpty()) {
            LocalDateTime minEventDate = result.stream().min(Comparator.comparing(EventShortDto::getEventDate)).orElseThrow().getEventDate();
            Map<String, EventShortDto> uris = result.stream()
                    .map(e -> Pair.of("/events/" + e.getId(), e))
                    .collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
            List<ViewStatsDto> stats = statsClient.stats(StatsRequestDto.builder()
                    .start(minEventDate)
                    .end(LocalDateTime.now())
                    .unique(true)
                    .uri(uris.keySet().stream().toList())
                    .build());
            for (ViewStatsDto stat : stats) {
                EventShortDto eventShortDto = uris.get(stat.getUri());
                eventShortDto.setViews((int) stat.getHits());
            }
        }

        saveEndpointHit(param.getRequest());
        return result;
    }

    @Transactional
    public EventFullDto get(Long id, RequestContext requestContext) {
        Event event = eventRepository.findById(id).orElseThrow();
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new NotFoundException("");
        }
        saveEndpointHit(requestContext);
        List<ViewStatsDto> stats = statsClient.stats(StatsRequestDto.builder()
                .start(event.getCreatedOn())
                .end(LocalDateTime.now())
                .unique(true)
                .uri(List.of("/events/" + id))
                .build());
        int confirmedRequests = requestRepository.countByEventIdAndStatus(id, RequestState.CONFIRMED);
        EventFullDto result = EventMapper.toEventFullDto(event, confirmedRequests);
        if (!stats.isEmpty()) {
            result.setViews((int) stats.get(0).getHits());
        }
        return result;
    }

    private void saveEndpointHit(RequestContext requestContext) {

        EndpointHitDto endpointHit = EndpointHitDto.builder()
                .ip(requestContext.getIp())
                .uri(requestContext.getUri())
                .app(serviceName)
                .timestamp(LocalDateTime.now())
                .build();
        statsClient.hit(endpointHit);
    }

    @Override
    @Transactional
    public EventFullDto update(Long eventId, UpdateEventAdminRequest updateEvent) {
        Event event = eventRepository.findById(eventId).orElseThrow();
        if (StateAction.PUBLISH_EVENT.equals(updateEvent.getStateAction()) &&
                !event.getState().equals(EventState.PENDING)) {
            throw new ConflictException("Невозможно опубликовать событие");
        }
        if (StateAction.REJECT_EVENT.equals(updateEvent.getStateAction()) &&
                event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Невозможно отменить опубликованное событие");
        }

        Event updatedEvent = EventMapper.toEventUpdate(updateEvent, event);
        int confirmedRequests = requestRepository.countByEventIdAndStatus(eventId, RequestState.CONFIRMED);
        return EventMapper.toEventFullDto(eventRepository.save(updatedEvent), confirmedRequests);
    }

    @Override
    @Transactional
    public List<EventShortDto> getAllShortEvents(Long userId, Integer from, Integer size) {
        PageRequest request = PageRequest.of(from / size, size,
                Sort.by(Sort.Direction.ASC, "id"));
        List<EventShortDto> dtoList;
        if (userId != null) {
            dtoList = EventMapper.toEventShortDtoList(eventRepository.findAllByInitiatorId(userId, request));
        } else {
            dtoList = EventMapper.toEventShortDtoList(eventRepository.findAll(request).getContent());
        }
        return dtoList;
    }

    @Override
    @Transactional
    public EventFullDto get(Long userId, Long eventId) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId);
        int confirmedRequests = requestRepository.countByEventIdAndStatus(eventId, RequestState.CONFIRMED);
        return EventMapper.toEventFullDto(event, confirmedRequests);
    }

    @Override
    @Transactional
    public List<ParticipationRequestDto> getRequests(Long userId, Long eventId) {
        if (!eventRepository.existsByIdAndInitiatorId(eventId, userId)) {
            throw new NotFoundException(String.format("Событие не найденное с id = %s и идентификатором пользователя = %s", eventId, userId));
        }
        return RequestMapper.toDtoList(requestRepository.findAllByEventId(eventId));
    }

    @Override
    @Transactional
    public EventFullDto create(Long userId, NewEventDto eventDto) {
        User user = userRepository.findById(userId).orElseThrow();
        if (eventDto.getEventDate() != null) {
            checkEventDate(eventDto.getEventDate());
        }
        return EventMapper.toEventFullDto(eventRepository.save(EventMapper.toEvent(eventDto, user)), 0);
    }

    @Override
    @Transactional
    public EventFullDto update(Long userId, Long eventId, UpdateEventUserRequest updateRequest) {
        userRepository.findById(userId).orElseThrow();
        Event event = eventRepository.findById(eventId).orElseThrow();
        if (event.getState().equals(PUBLISHED)) {
            throw new ConflictException("Событие уже опубликовано");
        }
        if (!event.getInitiator().getId().equals(userId)) {
            throw new NullPointerException("");
        }
        if (updateRequest.getEventDate() != null) {
            checkEventDate(updateRequest.getEventDate());
        }
        Event eventUpdate = EventMapper.toEventUpdate(updateRequest, event);
        int confirmedRequests = requestRepository.countByEventIdAndStatus(eventId, RequestState.CONFIRMED);
        return EventMapper.toEventFullDto(eventRepository.save(eventUpdate), confirmedRequests);
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateRequestStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest request) {
        userRepository.findById(userId).orElseThrow();
        Event event = eventRepository.findById(eventId).orElseThrow();
        if (event.getEventDate() != null) {
            checkEventDate(event.getEventDate());
        }
        List<Request> allRequest = requestRepository.findAllById(request.getRequestIds());

        List<Request> requestToConfirm = new ArrayList<>();
        List<Request> requestToReject = new ArrayList<>();

        switch (request.getStatus()) {
            case CONFIRMED -> {
                if (event.getParticipantLimit() > 0) {
                    int available = event.getParticipantLimit() -
                            requestRepository.countByEventIdAndStatus(eventId, RequestState.CONFIRMED);
                    if (available == 0) {
                        throw new ConflictException("Лимит участников исчерпан");
                    }
                    if (request.getRequestIds().size() < available) {
                        requestToConfirm.addAll(allRequest);
                    } else {
                        requestToConfirm.addAll(allRequest.subList(0, available));
                        requestToReject.addAll(allRequest.subList(available, allRequest.size()));
                    }
                } else {
                    requestToConfirm.addAll(allRequest);
                }
            }
            case REJECTED -> {
                for (Request r : allRequest) {
                    if (r.getStatus().equals(RequestState.CONFIRMED)) {
                        throw new ConflictException("Попытка отменить уже принятую заявку на участие в событии");
                    }
                }
                requestToReject.addAll(allRequest);
            }
        }

        requestToConfirm.forEach(r -> r.setStatus(RequestState.CONFIRMED));
        requestRepository.saveAll(requestToConfirm);
        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>(toRequestDto(requestToConfirm, RequestState.CONFIRMED));

        if (requestRepository.countByEventIdAndStatus(eventId, RequestState.CONFIRMED) == event.getParticipantLimit()) {
            // надо отклонить все оставшиеся заявки
            List<Request> allPendingRequests = requestRepository.findAllByEventIdAndStatus(eventId, RequestState.PENDING);
            requestToReject.addAll(allPendingRequests);
        }

        requestToReject.forEach(r -> r.setStatus(RequestState.REJECTED));
        requestRepository.saveAll(requestToReject);
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>(toRequestDto(requestToReject, RequestState.REJECTED));

        return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
    }

    private static List<ParticipationRequestDto> toRequestDto(List<Request> requests, RequestState toState) {
        return requests.stream().map(r -> new ParticipationRequestDto(r.getId(), r.getCreated(), r.getEvent().getId(), r.getRequester().getId(), toState)).toList();
    }

    private void checkEventDate(LocalDateTime eventDate) {
        if (eventDate.isBefore(LocalDateTime.now().plusHours(1))) {
            throw new ValidationException("Дата начала события должна быть не ранее чем за час от даты публикации");
        }
    }

}
