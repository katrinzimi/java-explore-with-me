package ru.practicum.explorewithme.server.publicAPI.servise.categoty;

import ru.practicum.explorewithme.server.dto.category.CategoryDto;

import java.util.List;

public interface PublicCategoriesService {
    List<CategoryDto> getAll(int from, int size);

    CategoryDto get(Long catId);
}
