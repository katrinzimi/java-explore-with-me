package ru.practicum.explorewithme.server.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewCategoryDto {
    @NotBlank
    @Size(min = 1, max = 50)
    private String name;

    @Override
    public String toString() {
        return "NewCategoryDto{" +
                "name='" + name + '\'' +
                '}';
    }
}
