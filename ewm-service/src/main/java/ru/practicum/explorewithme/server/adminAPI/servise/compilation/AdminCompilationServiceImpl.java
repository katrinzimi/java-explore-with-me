package ru.practicum.explorewithme.server.adminAPI.servise.compilation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.server.dto.compilation.CompilationDto;
import ru.practicum.explorewithme.server.dto.compilation.NewCompilationDto;
import ru.practicum.explorewithme.server.dto.compilation.UpdateCompilationRequest;
import ru.practicum.explorewithme.server.model.Event;
import ru.practicum.explorewithme.server.repository.EventRepository;

import java.util.Set;

@Service
@AllArgsConstructor
public class AdminCompilationServiceImpl implements AdminCompilationService {
   // private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    public CompilationDto save(NewCompilationDto createDto) {
        return null;
    }

    @Override
    public void delete(Long compId) {
    }

    @Override
    public CompilationDto update(Long compId, UpdateCompilationRequest updateCompilationRequest) {
        return null;
    }

    private Set<Event> findEvents(Set<Long> eventsId) {
        return null;
    }
}
