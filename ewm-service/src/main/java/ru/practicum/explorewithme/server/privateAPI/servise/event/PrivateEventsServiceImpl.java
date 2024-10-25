package ru.practicum.explorewithme.server.privateAPI.servise.event;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.server.adminAPI.exception.NotFoundException;
import ru.practicum.explorewithme.server.dto.event.*;
import ru.practicum.explorewithme.server.dto.mapper.EventMapper;
import ru.practicum.explorewithme.server.dto.mapper.RequestMapper;
import ru.practicum.explorewithme.server.exception.ValidationException;
import ru.practicum.explorewithme.server.model.Event;
import ru.practicum.explorewithme.server.model.Request;
import ru.practicum.explorewithme.server.model.User;
import ru.practicum.explorewithme.server.model.enums.EventState;
import ru.practicum.explorewithme.server.model.enums.RequestState;
import ru.practicum.explorewithme.server.repository.EventRepository;
import ru.practicum.explorewithme.server.repository.RequestRepository;
import ru.practicum.explorewithme.server.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class PrivateEventsServiceImpl implements PrivateEventsService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;

    @Override
    public Set<EventShortDto> getAll(Long userId, Integer from, Integer size) {
        PageRequest request = PageRequest.of(from / size, size);
        Set<EventShortDto> dtoSet;
        if (userId != null) {
            dtoSet = EventMapper.toEventShortDtoList(eventRepository.findAllByInitiatorId(userId, request));
        } else {
            dtoSet = EventMapper.toEventShortDtoList(eventRepository.findAll(request).stream().toList());
        }
        return dtoSet;
    }

    @Override
    public EventFullDto get(Long userId, Long eventId) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId);
        return EventMapper.toEventFullDto(event);
    }

    @Override
    public List<ParticipationRequestDto> getRequests(Long userId, Long eventId) {
        if (!eventRepository.existsByIdAndInitiatorId(eventId, userId)) {
            throw new NotFoundException(
                    String.format("Событие не найденное с id = %s и идентификатором пользователя = %s",
                            eventId, userId));
        }
        return RequestMapper.toDtoList(requestRepository.findAllByEventId(eventId));
    }

    @Override
    public EventFullDto create(Long userId, NewEventDto eventDto) {
        User user = userRepository.findById(userId).orElseThrow();
        return EventMapper.toEventFullDto(eventRepository.save(EventMapper.toEvent(eventDto, user)));
    }

    @Override
    public EventFullDto update(Long userId, Long eventId, UpdateEventUserRequest updateRequest) {

        userRepository.findById(userId).orElseThrow();
        Event event = eventRepository.findById(eventId).orElseThrow();
        if (!event.getInitiator().getId().equals(userId)) {
            throw new NullPointerException("");
        }
        Event updatedEvent = EventMapper.toEventUpdate(updateRequest, event);
        return EventMapper.toEventFullDto(eventRepository.save(updatedEvent));
    }

    @Override
    public EventRequestStatusUpdateResult updateRequestStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow();
        Event event = eventRepository.findById(eventId).orElseThrow();
        if (event.getEventDate() != null) {
            checkEventDate(event.getEventDate());
        }
        List<Request> allRequest = requestRepository.findAllById(request.getRequestIds());

        List<Request> requestToConfirm = new ArrayList<>();
        List<Request> requestToReject = new ArrayList<>();

        switch (request.getStatus()) {
            case CONFIRMED -> {
                if (event.getParticipantLimit() != null && event.getParticipantLimit() > 0) {
                    int available = event.getParticipantLimit() - event.getConfirmedRequests();

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
                requestToReject.addAll(allRequest);
            }
        }

        requestToConfirm.forEach(r -> r.setStatus(RequestState.CONFIRMED));
        requestRepository.saveAll(requestToConfirm);
        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>(toRequestDto(requestToConfirm, RequestState.CONFIRMED));

        event.setConfirmedRequests(event.getConfirmedRequests() + requestToConfirm.size());
        eventRepository.save(event);

        if (event.getConfirmedRequests() == event.getParticipantLimit()) {
            // надо отклонить все оставшиеся заявки
            List<Request> allPendingRequests = requestRepository.findAllByEventIdAndStatus(eventId, EventState.PENDING);
            requestToReject.addAll(allPendingRequests);
        }

        requestToReject.forEach(r -> r.setStatus(RequestState.REJECTED));
        requestRepository.saveAll(requestToReject);
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>(toRequestDto(requestToReject, RequestState.REJECTED));

        return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
    }

    private static List<ParticipationRequestDto> toRequestDto(List<Request> requests, RequestState toState) {
        return requests.stream()
                .map(r -> new ParticipationRequestDto(r.getId(), r.getCreated(), r.getEvent().getId(),
                        r.getRequester().getId(), toState))
                .toList();
    }

    private void checkEventDate(LocalDateTime eventDate) {
        if (eventDate.isBefore(LocalDateTime.now().plusHours(1))) {
            throw new ValidationException("Дата начала события должна быть не ранее чем за час от даты публикации");
        }
    }
}
