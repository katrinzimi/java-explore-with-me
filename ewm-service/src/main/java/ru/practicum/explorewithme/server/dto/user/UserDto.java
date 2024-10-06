package ru.practicum.explorewithme.server.dto.user;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private String email;

    private Long id;

    private String name;
}
