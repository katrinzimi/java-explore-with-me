package ru.practicum.explorewithme.server.adminAPI.servise.compilation;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.server.adminAPI.exception.NotFoundException;
import ru.practicum.explorewithme.server.dto.compilation.CompilationDto;
import ru.practicum.explorewithme.server.dto.compilation.NewCompilationDto;
import ru.practicum.explorewithme.server.dto.compilation.UpdateCompilationRequest;
import ru.practicum.explorewithme.server.dto.mapper.CompilationMapper;
import ru.practicum.explorewithme.server.exception.ConflictException;
import ru.practicum.explorewithme.server.model.Compilation;
import ru.practicum.explorewithme.server.model.Event;
import ru.practicum.explorewithme.server.repository.CompilationRepository;
import ru.practicum.explorewithme.server.repository.EventRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class AdminCompilationServiceImpl implements AdminCompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    public CompilationDto save(NewCompilationDto createDto) {
        if (createDto.getPinned() == null) {
            createDto.setPinned(false);
        }
        if (createDto.getEvents() == null) {
            createDto.setEvents(List.of());
        }
        Compilation compilation = CompilationMapper.toCompilation(createDto);
        compilation.setEvents(findEvents(createDto.getEvents()));
        try {
            compilation = compilationRepository.save(compilation);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(e.getMessage(), e);
        }
        return CompilationMapper.toCompilationDto(compilation);
    }

    @Override
    public void delete(Long compId) {
        compilationRepository.deleteById(compId);
    }

    @Override
    public CompilationDto update(Long compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(""));
        Compilation mapped = CompilationMapper.toCompilationUpdate(updateCompilationRequest, compilation);
        Compilation updatedCategory = compilationRepository.save(mapped);
        return CompilationMapper.toCompilationDto(updatedCategory);
    }

    private List<Event> findEvents(List<Long> eventsId) {
        if (eventsId == null) {
            return List.of();
        }
        return eventRepository.findAllByIdIn(eventsId);
    }

}
