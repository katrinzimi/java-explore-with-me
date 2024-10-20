package ru.practicum.explorewithme.server.privateAPI.servise.event;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.server.adminAPI.exception.NotFoundException;
import ru.practicum.explorewithme.server.dto.event.*;
import ru.practicum.explorewithme.server.dto.mapper.CompilationMapper;
import ru.practicum.explorewithme.server.dto.mapper.EventMapper;
import ru.practicum.explorewithme.server.dto.mapper.RequestMapper;
import ru.practicum.explorewithme.server.model.Compilation;
import ru.practicum.explorewithme.server.model.Event;
import ru.practicum.explorewithme.server.model.Request;
import ru.practicum.explorewithme.server.model.User;
import ru.practicum.explorewithme.server.model.enums.EventRequestStatus;
import ru.practicum.explorewithme.server.model.enums.EventState;
import ru.practicum.explorewithme.server.model.enums.RequestState;
import ru.practicum.explorewithme.server.repository.EventRepository;
import ru.practicum.explorewithme.server.repository.RequestRepository;
import ru.practicum.explorewithme.server.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
            dtoSet = eventRepository.findAllByRequesterId(userId, request);
        } else {
            dtoSet = EventMapper.toEventShortDtoList(eventRepository.findAll(request).toSet());
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
    public EventRequestStatusUpdateResult updateRequestStatus(Long userId, Long eventId,
                                                              EventRequestStatusUpdateRequest request) {


        //  10
//        request.getRequestIds().size();

        User user = userRepository.findById(userId).orElseThrow();
        Event event = eventRepository.findById(eventId).orElseThrow();
        List<Request> allRequest = requestRepository.findAllById(request.getRequestIds());

        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();

        if (request.getStatus().equals(EventRequestStatus.CONFIRMED)) {
            if (event.getParticipantLimit() != null && event.getParticipantLimit() > 0) {
                int available = event.getParticipantLimit() - event.getConfirmedRequests();

                if (request.getRequestIds().size() < available) {
                    allRequest.forEach(request1 -> request1.setStatus(RequestState.CONFIRMED));
                    requestRepository.saveAll(allRequest);
                    confirmedRequests.addAll(allRequest.stream()
                            .map(r -> new ParticipationRequestDto(r.getId(), r.getCreated(), r.getEvent().getId(),
                                    r.getRequester().getId(), RequestState.CONFIRMED))
                            .toList());
                    event.setConfirmedRequests(event.getConfirmedRequests() + request.getRequestIds().size());
                    eventRepository.save(event);

                } else {
                    List<Request> requestToConfirm = allRequest.subList(0, available);
                    List<Request> requestToReject = allRequest.subList(available, allRequest.size());

                    requestToConfirm.forEach(request1 -> request1.setStatus(RequestState.CONFIRMED));
                    requestRepository.saveAll(allRequest);
                    confirmedRequests.addAll(allRequest.stream()
                            .map(r -> new ParticipationRequestDto(r.getId(), r.getCreated(), r.getEvent().getId(),
                                    r.getRequester().getId(), RequestState.CONFIRMED))
                            .toList());

                    requestToReject.forEach(request1 -> request1.setStatus(RequestState.REJECTED));
                    requestRepository.saveAll(allRequest);
                    rejectedRequests.addAll(allRequest.stream()
                            .map(r -> new ParticipationRequestDto(r.getId(), r.getCreated(), r.getEvent().getId(),
                                    r.getRequester().getId(), RequestState.REJECTED))
                            .toList());

                    event.setConfirmedRequests(event.getConfirmedRequests() + available);
                    eventRepository.save(event);

                }


            } else {
                allRequest.forEach(request1 -> request1.setStatus(RequestState.CONFIRMED));
                requestRepository.saveAll(allRequest);
                confirmedRequests.addAll(allRequest.stream()
                        .map(r -> new ParticipationRequestDto(r.getId(), r.getCreated(), r.getEvent().getId(),
                                r.getRequester().getId(), RequestState.CONFIRMED))
                        .toList());

                event.setConfirmedRequests(event.getConfirmedRequests() + request.getRequestIds().size());
                eventRepository.save(event);
            }
        }
        if (request.getStatus().equals(EventRequestStatus.REJECTED)) {
            allRequest.forEach(request1 -> request1.setStatus(RequestState.REJECTED));
            requestRepository.saveAll(allRequest);
            rejectedRequests.addAll(allRequest.stream()
                    .map(r -> new ParticipationRequestDto(r.getId(), r.getCreated(), r.getEvent().getId(),
                            r.getRequester().getId(), RequestState.REJECTED))
                    .toList());
        }

        if (event.getConfirmedRequests() == event.getParticipantLimit()) {
            // надо отклонить все оставшиеся заявки

            List<Request> allPendingRequests = requestRepository.findAllByEventAndState(eventId, EventState.PENDING);
            allPendingRequests.forEach(r -> r.setStatus(RequestState.REJECTED));
            requestRepository.saveAll(allPendingRequests);
            rejectedRequests.addAll(allPendingRequests.stream()
                    .map(r -> new ParticipationRequestDto(r.getId(), r.getCreated(), r.getEvent().getId(),
                            r.getRequester().getId(), RequestState.REJECTED))
                    .toList());
        }


        return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
    }
}
