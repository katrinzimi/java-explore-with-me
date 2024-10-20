package ru.practicum.explorewithme.server.adminAPI.servise.event;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.server.dto.event.EventFullDto;
import ru.practicum.explorewithme.server.dto.event.UpdateEventAdminRequest;
import ru.practicum.explorewithme.server.dto.mapper.EventMapper;
import ru.practicum.explorewithme.server.model.Event;
import ru.practicum.explorewithme.server.publicAPI.dto.RequestParamEvent;
import ru.practicum.explorewithme.server.repository.EventRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository repository;

    @Override
    public EventFullDto update(Long eventId, UpdateEventAdminRequest updateEvent) {
        Event event = repository.findById(eventId).orElseThrow();
        Event updatedEvent = EventMapper.toEventUpdate(updateEvent, event);
        return EventMapper.toEventFullDto(repository.save(updatedEvent));
    }

    @Override
    public List<EventFullDto> getAll(RequestParamEvent param) {
        return repository.findAllByCriteria(param).getContent().stream().map(
                EventMapper::toEventFullDto
        ).collect(Collectors.toList());
    }
}
