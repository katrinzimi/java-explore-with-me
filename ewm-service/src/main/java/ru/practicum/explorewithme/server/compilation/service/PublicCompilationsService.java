package ru.practicum.explorewithme.server.compilation.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.explorewithme.server.compilation.dto.CompilationDto;

import java.util.List;

public interface PublicCompilationsService {
    List<CompilationDto> getAll(Boolean pinned, Pageable pageable);

    CompilationDto get(Long comId);
}
