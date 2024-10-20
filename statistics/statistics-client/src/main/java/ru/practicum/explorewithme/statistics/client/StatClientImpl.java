package ru.practicum.explorewithme.statistics.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.explorewithme.statistics.dto.EndpointHitDto;
import ru.practicum.explorewithme.statistics.dto.StatsRequestDto;
import ru.practicum.explorewithme.statistics.dto.ViewStatsDto;

import java.util.List;

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
            var uri = UriComponentsBuilder.fromUriString(serverUrl)
                    .path("/stats")
                    .queryParam("start", requestDto.getStart())
                    .queryParam("end", requestDto.getEnd())
                    .queryParam("uri", requestDto.getUri())
                    .queryParam("unique", requestDto.getUnique())
                    .build()
                    .toUri();
            RequestEntity<Void> requestEntity = RequestEntity.get(uri).build();
            ResponseEntity<List<ViewStatsDto>> response = restTemplate.exchange(requestEntity,
                    new ParameterizedTypeReference<>() {
                    });
            return response.getBody();
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

