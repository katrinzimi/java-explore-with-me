package ru.practicum.explorewithme.server.mapper;

import ru.practicum.explorewithme.server.category.dto.CategoryDto;
import ru.practicum.explorewithme.server.category.dto.NewCategoryDto;
import ru.practicum.explorewithme.server.model.Category;

public class CategoryMapper {
    public static CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName());
    }

    public static Category toCategory(NewCategoryDto newCategoryDto) {
        return new Category(
                null,
                newCategoryDto.getName());
    }

    public static Category toCategory(NewCategoryDto newCategoryDto, Category category) {
        return new Category(
                category.getId(),
                newCategoryDto.getName());
    }
}

