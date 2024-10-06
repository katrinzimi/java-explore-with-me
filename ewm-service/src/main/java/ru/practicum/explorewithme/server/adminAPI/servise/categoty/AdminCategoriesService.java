package ru.practicum.explorewithme.server.adminAPI.servise.categoty;

import ru.practicum.explorewithme.server.dto.category.CategoryDto;
import ru.practicum.explorewithme.server.dto.category.NewCategoryDto;

public interface AdminCategoriesService {
    CategoryDto create(NewCategoryDto dto);

    void delete(Long catId);

    CategoryDto update(NewCategoryDto dto, Long catId);
}
