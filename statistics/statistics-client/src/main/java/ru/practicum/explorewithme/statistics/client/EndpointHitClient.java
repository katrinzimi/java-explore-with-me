package ru.practicum.explorewithme.statistics.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.explorewithme.statistics.dto.EndpointHitDto;

import java.util.Map;

@Service
public class EndpointHitClient extends BaseClient {

    @Autowired
    public EndpointHitClient(@Value("${statistics-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> stats(String start, String end, String uri, String hit) {
        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "uri", uri,
                "hit", hit
        );
        return get("/hit?start={start}&end={end}&uri={uri}&hit={hit}", parameters);
    }


    public ResponseEntity<Object> hit(EndpointHitDto endpointHit) {
        return post("/hit", endpointHit);
    }

}

