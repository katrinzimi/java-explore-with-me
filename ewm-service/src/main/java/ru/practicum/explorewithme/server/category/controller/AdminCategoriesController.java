package ru.practicum.explorewithme.server.category.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.server.category.service.AdminCategoriesService;
import ru.practicum.explorewithme.server.category.dto.CategoryDto;
import ru.practicum.explorewithme.server.category.dto.NewCategoryDto;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin/categories")
public class AdminCategoriesController {

    public final AdminCategoriesService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto create(@RequestBody @Valid NewCategoryDto dto) {
        log.info("Получен запрос создание новой категории: {}", dto.getName());
        return service.create(dto);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long catId) {
        log.info("Получен запрос удаление категории по id {}", catId);
        service.delete(catId);
    }

    @PatchMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto update(@RequestBody @Valid NewCategoryDto dto, @PathVariable Long catId) {
        log.info("Получен запрос на изменение категориии: {}", dto.getName());
        return service.update(dto, catId);
    }
}