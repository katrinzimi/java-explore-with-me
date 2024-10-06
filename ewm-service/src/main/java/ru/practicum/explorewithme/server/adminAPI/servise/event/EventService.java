package ru.practicum.explorewithme.server.adminAPI.servise.event;

import ru.practicum.explorewithme.server.adminAPI.dto.RequestParamForEvent;
import ru.practicum.explorewithme.server.dto.event.EventFullDto;
import ru.practicum.explorewithme.server.dto.event.UpdateEventAdminRequest;

import java.util.List;

public interface EventService {
    EventFullDto update(Long eventId, UpdateEventAdminRequest updateEvent);

    List<EventFullDto> getAll(RequestParamForEvent param);
}
