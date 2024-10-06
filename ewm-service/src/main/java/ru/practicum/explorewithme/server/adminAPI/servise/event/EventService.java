package ru.practicum.explorewithme.server.adminAPI.servise.event;

import ru.practicum.explorewithme.server.dto.event.EventFullDto;
import ru.practicum.explorewithme.server.dto.event.UpdateEventAdminRequest;
import ru.practicum.explorewithme.server.publicAPI.dto.RequestParamEvent;

import java.util.List;

public interface EventService {
    EventFullDto update(Long eventId, UpdateEventAdminRequest updateEvent);

    List<EventFullDto> getAll(RequestParamEvent param);
}
