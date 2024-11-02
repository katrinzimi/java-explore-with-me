package ru.practicum.explorewithme.server.event.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.server.event.dto.EventFullDto;
import ru.practicum.explorewithme.server.event.dto.UpdateEventAdminRequest;
import ru.practicum.explorewithme.server.mapper.EventMapper;
import ru.practicum.explorewithme.server.exception.ConflictException;
import ru.practicum.explorewithme.server.model.Event;
import ru.practicum.explorewithme.server.model.enums.EventState;
import ru.practicum.explorewithme.server.model.enums.RequestState;
import ru.practicum.explorewithme.server.model.enums.StateAction;
import ru.practicum.explorewithme.server.event.dto.RequestParamEvent;
import ru.practicum.explorewithme.server.repository.EventRepository;
import ru.practicum.explorewithme.server.repository.RequestRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository repository;
    private final RequestRepository requestRepository;

    @Override
    @Transactional
    public EventFullDto update(Long eventId, UpdateEventAdminRequest updateEvent) {
        Event event = repository.findById(eventId).orElseThrow();
        if (StateAction.PUBLISH_EVENT.equals(updateEvent.getStateAction()) &&
                !event.getState().equals(EventState.PENDING)) {
            throw new ConflictException("Невозможно опубликовать событие");
        }
        if (StateAction.REJECT_EVENT.equals(updateEvent.getStateAction()) &&
                event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Невозможно отменить опубликованное событие");
        }

        Event updatedEvent = EventMapper.toEventUpdate(updateEvent, event);
        int confirmedRequests = requestRepository.countByEventIdAndStatus(eventId, RequestState.CONFIRMED);
        return EventMapper.toEventFullDto(repository.save(updatedEvent), confirmedRequests);
    }

    @Override
    @Transactional
    public List<EventFullDto> getAll(RequestParamEvent param) {
        List<Event> events = repository.findAllByCriteria(param).getContent();
        return events.stream()
                .map(event -> EventMapper.toEventFullDto(event,
                        requestRepository.countByEventIdAndStatus(event.getId(), RequestState.CONFIRMED)))
                .collect(Collectors.toList());
    }

}
