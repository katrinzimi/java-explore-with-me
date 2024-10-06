package ru.practicum.explorewithme.server.adminAPI.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.server.adminAPI.servise.compilation.AdminCompilationService;
import ru.practicum.explorewithme.server.dto.compilation.CompilationDto;
import ru.practicum.explorewithme.server.dto.compilation.NewCompilationDto;
import ru.practicum.explorewithme.server.dto.compilation.UpdateCompilationRequest;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/admin/compilations")
public class AdminCompilationsController {
    private final AdminCompilationService service;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto save(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        log.info("Получен запрос создания c новой подборкой: {}", newCompilationDto.getTitle());
        return service.save(newCompilationDto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long compId) {
        log.info("Получен запрос удаление");
        service.delete(compId);
    }

    @PatchMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto update(@PathVariable Long compId,
                                 @RequestBody @Valid UpdateCompilationRequest updateCompilationRequest) {
        log.info("Получен запрос на изменение подборки.");
        return service.update(compId, updateCompilationRequest);
    }
}
