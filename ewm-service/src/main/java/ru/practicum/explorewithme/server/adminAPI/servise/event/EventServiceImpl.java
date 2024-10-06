package ru.practicum.explorewithme.server.adminAPI.servise.event;

import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.server.adminAPI.dto.RequestParamForEvent;
import ru.practicum.explorewithme.server.dto.event.EventFullDto;
import ru.practicum.explorewithme.server.dto.event.UpdateEventAdminRequest;

import java.util.List;
@Service
public class EventServiceImpl implements EventService{
    @Override
    public EventFullDto update(Long eventId, UpdateEventAdminRequest updateEvent) {
        return null;
    }

    @Override
    public List<EventFullDto> getAll(RequestParamForEvent param) {
        return null;
    }
}
