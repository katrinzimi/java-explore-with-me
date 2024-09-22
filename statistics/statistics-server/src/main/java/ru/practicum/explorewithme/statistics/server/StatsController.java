package ru.practicum.explorewithme.statistics.server;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.statistics.dto.EndpointHitDto;
import ru.practicum.explorewithme.statistics.dto.ViewStatsDto;
import ru.practicum.explorewithme.statistics.server.service.EndpointHitService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping()
public class StatsController {
    private final EndpointHitService service;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void hit(@RequestBody @Valid EndpointHitDto endpointHit) {
        log.info("Создание пользователя:  {} ", endpointHit);
        service.hit(endpointHit);
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.CREATED)
    public List<ViewStatsDto> stats(@RequestParam LocalDateTime start, @RequestParam LocalDateTime end,
                                    @RequestParam(required = false, defaultValue = "") List<String> uri,
                                    @RequestParam(required = false, defaultValue = "false") Boolean unique) {
        return service.getStats(start, end, uri, unique);
    }
}
