package ru.practicum.explorewithme.server.publicAPI.servise.event;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.server.dto.event.EventFullDto;
import ru.practicum.explorewithme.server.dto.event.EventShortDto;
import ru.practicum.explorewithme.server.dto.mapper.EventMapper;
import ru.practicum.explorewithme.server.publicAPI.dto.RequestParamEvent;
import ru.practicum.explorewithme.server.repository.EventRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PublicEventsServiceImpl implements PublicEventsService {
    private final EventRepository repository;

    @Override
    public List<EventShortDto> getAll(RequestParamEvent param) {
        return repository.findAllByCriteria(param).getContent().stream().map(
                EventMapper::toEventShortDto
        ).collect(Collectors.toList());
    }

    @Override
    public EventFullDto get(Long id) {
        return EventMapper.toEventFullDto(repository.findById(id).orElseThrow());
    }
}
