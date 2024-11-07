package ru.practicum.explorewithme.server.event.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private Long eventId;
    private Long userId;
    private String comment;
}
