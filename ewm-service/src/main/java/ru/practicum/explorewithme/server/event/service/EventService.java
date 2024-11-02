package ru.practicum.explorewithme.server.event.service;

import ru.practicum.explorewithme.server.event.dto.EventFullDto;
import ru.practicum.explorewithme.server.event.dto.UpdateEventAdminRequest;
import ru.practicum.explorewithme.server.event.dto.RequestParamEvent;

import java.util.List;

public interface EventService {
    EventFullDto update(Long eventId, UpdateEventAdminRequest updateEvent);

    List<EventFullDto> getAll(RequestParamEvent param);
}
