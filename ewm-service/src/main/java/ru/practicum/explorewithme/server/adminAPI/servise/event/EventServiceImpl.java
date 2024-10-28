package ru.practicum.explorewithme.server.adminAPI.servise.event;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.server.dto.event.EventFullDto;
import ru.practicum.explorewithme.server.dto.event.UpdateEventAdminRequest;
import ru.practicum.explorewithme.server.dto.mapper.EventMapper;
import ru.practicum.explorewithme.server.exception.ConflictException;
import ru.practicum.explorewithme.server.exception.ValidationException;
import ru.practicum.explorewithme.server.model.Event;
import ru.practicum.explorewithme.server.model.enums.EventState;
import ru.practicum.explorewithme.server.model.enums.StateAction;
import ru.practicum.explorewithme.server.publicAPI.dto.RequestParamEvent;
import ru.practicum.explorewithme.server.repository.EventRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository repository;

    @Override
    public EventFullDto update(Long eventId, UpdateEventAdminRequest updateEvent) {
        Event event = repository.findById(eventId).orElseThrow();
        if (updateEvent.getEventDate() != null) {
            checkEventDate(updateEvent.getEventDate());
        }
        if (StateAction.PUBLISH_EVENT.equals(updateEvent.getStateAction()) &&
                !event.getState().equals(EventState.PENDING)) {
            throw new ConflictException("Невозможно опубликовать событие");
        }
        if (StateAction.REJECT_EVENT.equals(updateEvent.getStateAction()) &&
                event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Невозможно отменить опубликованное событие");
        }

        Event updatedEvent = EventMapper.toEventUpdate(updateEvent, event);
        return EventMapper.toEventFullDto(repository.save(updatedEvent));
    }

    @Override
    public List<EventFullDto> getAll(RequestParamEvent param) {
        return repository.findAllByCriteria(param).getContent().stream().map(
                EventMapper::toEventFullDto
        ).collect(Collectors.toList());
    }

    private void checkEventDate(LocalDateTime eventDate) {
        if (eventDate.isBefore(LocalDateTime.now().plusHours(1))) {
            throw new ValidationException("Дата начала события должна быть не ранее чем за час от даты публикации");
        }
    }
}
