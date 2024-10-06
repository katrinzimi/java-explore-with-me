package ru.practicum.explorewithme.server.publicAPI.servise.categoty;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.server.dto.category.CategoryDto;
import ru.practicum.explorewithme.server.dto.mapper.CategoryMapper;
import ru.practicum.explorewithme.server.repository.CategoriesRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublicCategoriesServiceImpl implements PublicCategoriesService {
    CategoriesRepository repository;

    @Override
    public List<CategoryDto> getAll(int from, int size) {
        return repository.findAll(PageRequest.of(from / size, size))
                .getContent()
                .stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto get(Long catId) {
        return CategoryMapper.toCategoryDto(repository.findById(catId).orElseThrow());
    }
}
