package ru.practicum.explorewithme.server.adminAPI.servise.compilation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.server.adminAPI.exception.NotFoundException;
import ru.practicum.explorewithme.server.dto.compilation.CompilationDto;
import ru.practicum.explorewithme.server.dto.compilation.NewCompilationDto;
import ru.practicum.explorewithme.server.dto.compilation.UpdateCompilationRequest;
import ru.practicum.explorewithme.server.dto.mapper.CompilationMapper;
import ru.practicum.explorewithme.server.model.Compilation;
import ru.practicum.explorewithme.server.repository.CompilationRepository;

@Service
@AllArgsConstructor
public class AdminCompilationServiceImpl implements AdminCompilationService {
    private final CompilationRepository compilationRepository;

    @Override
    public CompilationDto save(NewCompilationDto createDto) {
        return CompilationMapper.toCompilationDto(
                compilationRepository.save(CompilationMapper.toCompilation(createDto)));
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

}
