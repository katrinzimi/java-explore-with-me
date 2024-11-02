package ru.practicum.explorewithme.server.category.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.explorewithme.server.category.dto.CategoryDto;

import java.util.List;

public interface PublicCategoriesService {
    List<CategoryDto> getAll(Pageable pageable);

    CategoryDto get(Long catId);
}
