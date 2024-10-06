package ru.practicum.explorewithme.server.dto.compilation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.explorewithme.server.dto.event.EventShortDto;

import java.util.List;
@AllArgsConstructor
@Data
@Builder
public class CompilationDto {
    private long id;
    private String title;
    private Boolean pinned;
    private List<EventShortDto> events;
}
