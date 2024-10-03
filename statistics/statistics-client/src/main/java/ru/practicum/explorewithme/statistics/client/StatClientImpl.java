package ru.practicum.explorewithme.statistics.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.practicum.explorewithme.statistics.dto.EndpointHitDto;
import ru.practicum.explorewithme.statistics.dto.StatsRequestDto;
import ru.practicum.explorewithme.statistics.dto.ViewStatsDto;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class StatClientImpl implements StatClient {
    private final String serverUrl;
    private final RestTemplate restTemplate = new RestTemplate();

    public StatClientImpl(@Value("${statistics-server.url}") String serverUrl) {
        this.serverUrl = serverUrl;
    }

    @Override
    public List<ViewStatsDto> stats(StatsRequestDto requestDto) {
        try {
            Map<String, Object> parameters = Map.of(
                    "start", requestDto.getStart(),
                    "end", requestDto.getEnd(),
                    "uri", requestDto.getUri(),
                    "unique", requestDto.getUnique()
            );
            ViewStatsDto[] result = restTemplate.getForObject(serverUrl + "/stats?start={start}&end={end}&uri={uri}&unique={unique}",
                    ViewStatsDto[].class, parameters);
            return Arrays.stream(result).toList();
        } catch (Exception e) {
            log.error("Сервер статистики не доступен", e);
            return List.of();
        }
    }


    @Override
    public void hit(EndpointHitDto endpointHit) {
        try {
            restTemplate.postForObject(serverUrl + "/hit", endpointHit, ResponseEntity.class);
        } catch (Exception e) {
            log.error("Сервер статистики не доступен", e);
        }
    }

}

