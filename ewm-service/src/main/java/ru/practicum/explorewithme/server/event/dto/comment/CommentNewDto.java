package ru.practicum.explorewithme.server.event.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CommentNewDto {
    @NotNull
    private Long userId;
    @Length(min = 20, max = 1000)
    @NotBlank
    private String comment;
}
