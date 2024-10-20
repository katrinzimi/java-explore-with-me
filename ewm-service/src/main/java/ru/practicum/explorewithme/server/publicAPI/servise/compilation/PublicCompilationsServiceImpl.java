package ru.practicum.explorewithme.server.publicAPI.servise.compilation;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.server.dto.compilation.CompilationDto;
import ru.practicum.explorewithme.server.dto.mapper.CategoryMapper;
import ru.practicum.explorewithme.server.dto.mapper.CompilationMapper;
import ru.practicum.explorewithme.server.model.Compilation;
import ru.practicum.explorewithme.server.repository.CompilationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PublicCompilationsServiceImpl implements PublicCompilationsService {
    private final CompilationRepository repository;

    @Override
    public List<CompilationDto> getAll(Boolean pinned, int from, int size) {
        PageRequest request = PageRequest.of(from / size, size);
        List<Compilation> compilations;
        if (pinned != null) {
            compilations = repository.findAllByPinned(pinned, request);
        } else {
            compilations = repository.findAll(request).toList();
        }
        return compilations.stream().map(CompilationMapper::toCompilationDto).collect(Collectors.toList());
    }

    @Override
    public CompilationDto get(Long comId) {
        return CompilationMapper.toCompilationDto(repository.findById(comId).orElseThrow());
    }
}
