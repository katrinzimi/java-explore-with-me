package ru.practicum.explorewithme.server.compilation.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.explorewithme.server.compilation.dto.CompilationDto;
import ru.practicum.explorewithme.server.compilation.dto.NewCompilationDto;
import ru.practicum.explorewithme.server.compilation.dto.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {
    CompilationDto save(NewCompilationDto newCompilationDto);

    void delete(Long compId);

    CompilationDto update(Long compId, UpdateCompilationRequest updateCompilationRequest);

    List<CompilationDto> getAll(Boolean pinned, Pageable pageable);

    CompilationDto get(Long comId);
}
