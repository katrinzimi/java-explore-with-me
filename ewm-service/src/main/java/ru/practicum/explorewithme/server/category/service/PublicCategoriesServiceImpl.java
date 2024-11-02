package ru.practicum.explorewithme.server.category.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.server.exception.NotFoundException;
import ru.practicum.explorewithme.server.category.dto.CategoryDto;
import ru.practicum.explorewithme.server.mapper.CategoryMapper;
import ru.practicum.explorewithme.server.repository.CategoriesRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PublicCategoriesServiceImpl implements PublicCategoriesService {
    private final CategoriesRepository repository;

    @Override
    @Transactional
    public List<CategoryDto> getAll(Pageable pageable) {
        return repository.findAll(pageable)
                .getContent()
                .stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CategoryDto get(Long catId) {
        return CategoryMapper.toCategoryDto(repository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Категория не найдена")));
    }
}
