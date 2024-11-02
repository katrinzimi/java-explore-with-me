package ru.practicum.explorewithme.server.category.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.server.exception.NotFoundException;
import ru.practicum.explorewithme.server.category.dto.CategoryDto;
import ru.practicum.explorewithme.server.category.dto.NewCategoryDto;
import ru.practicum.explorewithme.server.mapper.CategoryMapper;
import ru.practicum.explorewithme.server.exception.ConflictException;
import ru.practicum.explorewithme.server.model.Category;
import ru.practicum.explorewithme.server.model.Event;
import ru.practicum.explorewithme.server.repository.CategoriesRepository;
import ru.practicum.explorewithme.server.repository.EventRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class AdminCategoriesServiceImpl implements AdminCategoriesService {
    private final CategoriesRepository categoriesRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CategoryDto create(NewCategoryDto dto) {
        if (categoriesRepository.existsByName(dto.getName())) {
            throw new ConflictException("Имя категории уже занято");
        }
        Category savedCategory = categoriesRepository.save(CategoryMapper.toCategory(dto));
        return CategoryMapper.toCategoryDto(savedCategory);
    }

    @Override
    @Transactional
    public void delete(Long catId) {
        List<Event> allByCategoryId = eventRepository.findAllByCategoryId(catId);
        if (!allByCategoryId.isEmpty()) {
            throw new ConflictException("Невозможно удаить категорию");
        }
        categoriesRepository.deleteById(catId);
    }

    @Override
    @Transactional
    public CategoryDto update(NewCategoryDto dto, Long catId) {
        Category oldCategory = categoriesRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Категории не существует"));
        if (!dto.getName().equals(oldCategory.getName()) && categoriesRepository.existsByName(dto.getName())) {
            throw new ConflictException("Категория с таким названием уже существует");
        }
        Category mapped = CategoryMapper.toCategory(dto, oldCategory);
        Category updatedCategory = categoriesRepository.save(mapped);
        return CategoryMapper.toCategoryDto(updatedCategory);
    }
}
