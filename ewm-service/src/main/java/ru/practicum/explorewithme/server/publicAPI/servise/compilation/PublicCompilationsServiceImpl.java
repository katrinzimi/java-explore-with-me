package ru.practicum.explorewithme.server.publicAPI.servise.compilation;

import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.server.dto.compilation.CompilationDto;

import java.util.List;
@Service
public class PublicCompilationsServiceImpl implements PublicCompilationsService{
    @Override
    public List<CompilationDto> getAll(Boolean pinned, int from, int size) {
        return null;
    }

    @Override
    public CompilationDto get(Long comId) {
        return null;
    }
}
