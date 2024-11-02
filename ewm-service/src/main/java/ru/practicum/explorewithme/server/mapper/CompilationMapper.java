package ru.practicum.explorewithme.server.mapper;

import ru.practicum.explorewithme.server.compilation.dto.CompilationDto;
import ru.practicum.explorewithme.server.compilation.dto.NewCompilationDto;
import ru.practicum.explorewithme.server.compilation.dto.UpdateCompilationRequest;
import ru.practicum.explorewithme.server.model.Compilation;
import ru.practicum.explorewithme.server.model.Event;

import java.util.List;
import java.util.stream.Collectors;

public class CompilationMapper {
    public static Compilation toCompilation(NewCompilationDto dto) {
        return Compilation.builder()
                .title(dto.getTitle())
                .pinned(dto.getPinned())
                .events(dto.getEvents().stream().map(
                        eventId -> Event.builder().id(eventId).build()
                ).collect(Collectors.toList()))
                .build();
    }

    public static CompilationDto toCompilationDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.getPinned())
                .events(compilation.getEvents().stream().map(
                        EventMapper::toEventShortDto
                ).collect(Collectors.toList()))
                .build();
    }

    public static Compilation toCompilationUpdate(UpdateCompilationRequest updateCompilationDto, Compilation compilation) {
        if (updateCompilationDto.getTitle() != null) {
            compilation.setTitle(updateCompilationDto.getTitle());
        }

        if (updateCompilationDto.getPinned() != null) {
            compilation.setPinned(updateCompilationDto.getPinned());
        }
        if (updateCompilationDto.getEvents() != null) {
            compilation.setEvents(updateCompilationDto.getEvents().stream().map(
                    eventId -> Event.builder().id(eventId).build()
            ).collect(Collectors.toList()));
        }
        return compilation;
    }

    public static List<CompilationDto> toEventShortDtoList(List<Compilation> compilations) {
        return compilations.stream().map(CompilationMapper::toCompilationDto).collect(Collectors.toList());
    }
}
