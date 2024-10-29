package ru.practicum.explorewithme.server.publicAPI.servise.categoty;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.server.adminAPI.exception.NotFoundException;
import ru.practicum.explorewithme.server.dto.category.CategoryDto;
import ru.practicum.explorewithme.server.dto.mapper.CategoryMapper;
import ru.practicum.explorewithme.server.repository.CategoriesRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PublicCategoriesServiceImpl implements PublicCategoriesService {
    private final CategoriesRepository repository;

    @Override
    public List<CategoryDto> getAll(int from, int size) {
        return repository.findAll(PageRequest.of(from / size, size,
                        Sort.by(Sort.Direction.ASC,"id")))
                .getContent()
                .stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto get(Long catId) {
        return CategoryMapper.toCategoryDto(repository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Категория не найдена")));
    }
}
