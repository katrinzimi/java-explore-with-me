package ru.practicum.explorewithme.server.event.service;

import ru.practicum.explorewithme.server.event.dto.*;

import java.util.List;

public interface EventService {
    EventFullDto update(Long eventId, UpdateEventAdminRequest updateEvent);

    List<EventFullDto> getAllFullEvents(RequestParamEvent param);

    List<EventShortDto> getAllShortEvents(Long userId, Integer from, Integer size);

    EventFullDto get(Long userId, Long eventId);

    List<ParticipationRequestDto> getRequests(Long userId, Long eventId);

    EventFullDto create(Long userId, NewEventDto eventDto);

    EventFullDto update(Long userId, Long eventId, UpdateEventUserRequest eventDto);

    EventRequestStatusUpdateResult updateRequestStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest request);

    EventFullDto get(Long id, RequestContext requestContext);

    List<EventShortDto> getAllShortEvents(RequestParamEvent param);
}
