package ru.practicum.explorewithme.server.publicAPI.servise.event;

import ru.practicum.explorewithme.server.dto.event.EventFullDto;
import ru.practicum.explorewithme.server.dto.event.EventShortDto;
import ru.practicum.explorewithme.server.publicAPI.dto.RequestParamEvent;

import java.util.List;

public interface PublicEventsService {
    List<EventShortDto> getAll(RequestParamEvent param);

    EventFullDto get(Long id);
}