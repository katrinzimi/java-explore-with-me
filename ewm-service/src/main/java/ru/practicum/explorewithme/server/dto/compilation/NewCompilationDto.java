package ru.practicum.explorewithme.server.dto.compilation;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

@Data
@NoArgsConstructor
public class NewCompilationDto {
    @NotBlank
    @Length(min = 1, max = 50)
    private String title;
    private Set<Long> events;
    private Boolean pinned;
}
