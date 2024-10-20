package ru.practicum.explorewithme.server.publicAPI.servise.compilation;

import ru.practicum.explorewithme.server.dto.compilation.CompilationDto;

import java.util.List;

public interface PublicCompilationsService {
    List<CompilationDto> getAll(Boolean pinned, int from, int size);

    CompilationDto get(Long comId);
}
