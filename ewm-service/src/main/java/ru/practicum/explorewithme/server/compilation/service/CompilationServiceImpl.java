package ru.practicum.explorewithme.server.compilation.service;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.server.compilation.dto.CompilationDto;
import ru.practicum.explorewithme.server.compilation.dto.NewCompilationDto;
import ru.practicum.explorewithme.server.compilation.dto.UpdateCompilationRequest;
import ru.practicum.explorewithme.server.exception.ConflictException;
import ru.practicum.explorewithme.server.exception.NotFoundException;
import ru.practicum.explorewithme.server.mapper.CompilationMapper;
import ru.practicum.explorewithme.server.model.Compilation;
import ru.practicum.explorewithme.server.model.Event;
import ru.practicum.explorewithme.server.repository.CompilationRepository;
import ru.practicum.explorewithme.server.repository.EventRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
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
    @Transactional
    public void delete(Long compId) {
        compilationRepository.deleteById(compId);
    }

    @Override
    @Transactional
    public CompilationDto update(Long compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Подборка событий не найдена"));
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

    @Override
    @Transactional
    public List<CompilationDto> getAll(Boolean pinned, Pageable pageable) {
        List<Compilation> compilations;
        if (pinned != null) {
            compilations = compilationRepository.findAllByPinned(pinned, pageable);
        } else {
            compilations = compilationRepository.findAll(pageable).toList();
        }
        return CompilationMapper.toEventShortDtoList(compilations);

    }

    @Override
    @Transactional
    public CompilationDto get(Long comId) {
        return CompilationMapper.toCompilationDto(compilationRepository.findById(comId)
                .orElseThrow(() -> new NullPointerException("Событие не найдено")));
    }

}
