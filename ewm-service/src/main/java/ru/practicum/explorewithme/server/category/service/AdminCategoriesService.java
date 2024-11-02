package ru.practicum.explorewithme.server.category.service;

import ru.practicum.explorewithme.server.category.dto.CategoryDto;
import ru.practicum.explorewithme.server.category.dto.NewCategoryDto;

public interface AdminCategoriesService {
    CategoryDto create(NewCategoryDto dto);

    void delete(Long catId);

    CategoryDto update(NewCategoryDto dto, Long catId);
}
