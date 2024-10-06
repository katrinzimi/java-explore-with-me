package ru.practicum.explorewithme.server.privateAPI.servise.event;

import ru.practicum.explorewithme.server.dto.event.*;

import java.util.List;
import java.util.Set;

public interface PrivateEventsService {
    Set<EventShortDto> getAll(Long userId, Integer from, Integer size);

    EventFullDto get(Long userId, Long eventId);

    List<ParticipationRequestDto> getRequests(Long userId, Long eventId);

    EventFullDto create(Long userId, NewEventDto eventDto);

    EventFullDto update(Long userId, Long eventId, UpdateEventUserRequest eventDto);

    EventRequestStatusUpdateResult updateRequestStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest request);
}
