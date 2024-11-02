package ru.practicum.explorewithme.server.compilation.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.server.compilation.dto.CompilationDto;
import ru.practicum.explorewithme.server.mapper.CompilationMapper;
import ru.practicum.explorewithme.server.model.Compilation;
import ru.practicum.explorewithme.server.repository.CompilationRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class PublicCompilationsServiceImpl implements PublicCompilationsService {
    private final CompilationRepository repository;

    @Override
    @Transactional
    public List<CompilationDto> getAll(Boolean pinned, Pageable pageable) {
        List<Compilation> compilations;
        if (pinned != null) {
            compilations = repository.findAllByPinned(pinned, pageable);
        } else {
            compilations = repository.findAll(pageable).toList();
        }
        return CompilationMapper.toEventShortDtoList(compilations);
//        return compilations.stream().map(CompilationMapper::toCompilationDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CompilationDto get(Long comId) {
        return CompilationMapper.toCompilationDto(repository.findById(comId)
                .orElseThrow(() -> new NullPointerException("Событие не найдено")));
    }
}
