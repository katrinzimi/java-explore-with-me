package ru.practicum.explorewithme.server.publicAPI.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.server.dto.compilation.CompilationDto;
import ru.practicum.explorewithme.server.publicAPI.servise.compilation.PublicCompilationsService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/compilations")
public class PublicCompilationsController {

    public final PublicCompilationsService compilationsService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CompilationDto> getAll(@RequestParam(required = false) Boolean pinned,
                                       @RequestParam(defaultValue = "0") int from,
                                       @RequestParam(defaultValue = "10") int size) {
        log.info("Получение списка подборки событий");
        return compilationsService.getAll(pinned, from, size);
    }

    @GetMapping("/{comId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto get(@PathVariable Long comId) {
        log.info("Получение подборки событий по её id {}", comId);
        return compilationsService.get(comId);
    }
}
