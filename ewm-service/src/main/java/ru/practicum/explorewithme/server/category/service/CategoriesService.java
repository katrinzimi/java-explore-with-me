package ru.practicum.explorewithme.server.category.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.explorewithme.server.category.dto.CategoryDto;
import ru.practicum.explorewithme.server.category.dto.NewCategoryDto;

import java.util.List;

public interface CategoriesService {
    CategoryDto create(NewCategoryDto dto);

    void delete(Long catId);

    CategoryDto update(NewCategoryDto dto, Long catId);

    List<CategoryDto> getAll(Pageable pageable);

    CategoryDto get(Long catId);
}
