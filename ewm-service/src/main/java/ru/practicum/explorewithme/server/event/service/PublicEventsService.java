package ru.practicum.explorewithme.server.event.service;

import ru.practicum.explorewithme.server.event.dto.RequestContext;
import ru.practicum.explorewithme.server.event.dto.EventFullDto;
import ru.practicum.explorewithme.server.event.dto.EventShortDto;
import ru.practicum.explorewithme.server.event.dto.RequestParamEvent;

import java.util.List;

public interface PublicEventsService {
    List<EventShortDto> getAll(RequestParamEvent param);

    EventFullDto get(Long id, RequestContext requestContext);
}
