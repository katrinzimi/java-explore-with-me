package ru.practicum.explorewithme.server.compilation.service;

import ru.practicum.explorewithme.server.compilation.dto.CompilationDto;
import ru.practicum.explorewithme.server.compilation.dto.NewCompilationDto;
import ru.practicum.explorewithme.server.compilation.dto.UpdateCompilationRequest;

public interface AdminCompilationService {
    CompilationDto save(NewCompilationDto newCompilationDto);

    void delete(Long compId);

    CompilationDto update(Long compId, UpdateCompilationRequest updateCompilationRequest);
}
