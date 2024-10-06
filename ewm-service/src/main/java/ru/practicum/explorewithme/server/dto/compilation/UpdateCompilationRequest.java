package ru.practicum.explorewithme.server.dto.compilation;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.Set;
@AllArgsConstructor
@Data
public class UpdateCompilationRequest {
    @Length(min = 1, max = 50)
    private String title;
    private Set<Long> events;
    private Boolean pinned;
}
