package ru.practicum.explorewithme.server.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewUserRequest {
    @NotBlank
    @Size(max = 128)
    private String name;
    @Email
    @NotBlank
    @Size(max = 255)
    private String email;
}