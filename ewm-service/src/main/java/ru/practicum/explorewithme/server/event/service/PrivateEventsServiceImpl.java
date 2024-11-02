package ru.practicum.explorewithme.server.event.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.server.event.dto.*;
import ru.practicum.explorewithme.server.exception.NotFoundException;
import ru.practicum.explorewithme.server.mapper.EventMapper;
import ru.practicum.explorewithme.server.mapper.RequestMapper;
import ru.practicum.explorewithme.server.exception.ConflictException;
import ru.practicum.explorewithme.server.exception.ValidationException;
import ru.practicum.explorewithme.server.model.Event;
import ru.practicum.explorewithme.server.model.Request;
import ru.practicum.explorewithme.server.model.User;
import ru.practicum.explorewithme.server.model.enums.RequestState;
import ru.practicum.explorewithme.server.repository.EventRepository;
import ru.practicum.explorewithme.server.repository.RequestRepository;
import ru.practicum.explorewithme.server.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ru.practicum.explorewithme.server.model.enums.EventState.PUBLISHED;

@Service
@AllArgsConstructor
public class PrivateEventsServiceImpl implements PrivateEventsService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;

    @Override
    @Transactional
    public List<EventShortDto> getAll(Long userId, Integer from, Integer size) {
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
                            requestRepository.countByEventIdAndStatus(eventId,RequestState.CONFIRMED);
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

        if (requestRepository.countByEventIdAndStatus(eventId,RequestState.CONFIRMED) == event.getParticipantLimit()) {
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
