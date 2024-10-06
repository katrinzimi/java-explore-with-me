package ru.practicum.explorewithme.server.privateAPI.servise.event;

import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.server.dto.event.*;

import java.util.List;
import java.util.Set;

@Service
public class PrivateEventsServiceImpl implements PrivateEventsService {
    @Override
    public Set<EventShortDto> getAll(Long userId, Integer from, Integer size) {
        return null;
    }

    @Override
    public EventFullDto get(Long userId, Long eventId) {
        return null;
    }

    @Override
    public List<ParticipationRequestDto> getRequests(Long userId, Long eventId) {
        return null;
    }

    @Override
    public EventFullDto create(Long userId, NewEventDto eventDto) {
        return null;
    }

    @Override
    public EventFullDto update(Long userId, Long eventId, UpdateEventUserRequest eventDto) {
        return null;
    }

    @Override
    public EventRequestStatusUpdateResult updateRequestStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest request) {
        return null;
    }
}
