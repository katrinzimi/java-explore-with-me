package ru.practicum.explorewithme.server.adminAPI.servise.categoty;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.server.dto.category.CategoryDto;
import ru.practicum.explorewithme.server.dto.category.NewCategoryDto;
import ru.practicum.explorewithme.server.dto.mapper.CategoryMapper;
import ru.practicum.explorewithme.server.adminAPI.exception.NotFoundException;
import ru.practicum.explorewithme.server.model.Category;
import ru.practicum.explorewithme.server.repository.CategoriesRepository;
@Service
@AllArgsConstructor
public class AdminCategoriesServiceImpl implements AdminCategoriesService {
    private final CategoriesRepository categoriesRepository;
    @Override
    public CategoryDto create(NewCategoryDto dto) {
        Category savedCategory = categoriesRepository.save(CategoryMapper.toCategory(dto));
        return CategoryMapper.toCategoryDto(savedCategory);
    }

    @Override
    public void delete(Long catId) {
        categoriesRepository.deleteById(catId);
    }

    @Override
    public CategoryDto update(NewCategoryDto dto, Long catId) {
        Category oldCategory = categoriesRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Категории не существует"));
        Category mapped = CategoryMapper.toCategory(dto,oldCategory);
        Category updatedCategory = categoriesRepository.save(mapped);
        return CategoryMapper.toCategoryDto(updatedCategory);
    }
}
