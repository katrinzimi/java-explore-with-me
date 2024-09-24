package ru.practicum.explorewithme.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import ru.practicum.explorewithme.statistics.client.StatClient;
import ru.practicum.explorewithme.statistics.dto.EndpointHitDto;
import ru.practicum.explorewithme.statistics.dto.StatsRequestDto;
import ru.practicum.explorewithme.statistics.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
@ComponentScan("ru.practicum.explorewithme.statistics.client")
public class Main {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Main.class, args);

        StatClient client = context.getBean(StatClient.class);

        client.hit(new EndpointHitDto("ewm-main-service", "/events/1",
                "192.163.0.1", LocalDateTime.parse("2022-09-06T11:00:23")));
        client.hit(new EndpointHitDto("ewm-main-service", "/events/1/1",
                "192.163.0.1", LocalDateTime.parse("2022-09-06T11:00:23")));
        client.hit(new EndpointHitDto("ewm-main-service", "/events/1/1",
                "192.163.0.1", LocalDateTime.parse("2022-09-06T11:00:23")));

        List<ViewStatsDto> stats = client.stats(new StatsRequestDto(LocalDateTime.parse("2022-09-06T11:00:23"),
                LocalDateTime.parse("2022-09-06T11:00:23"), List.of("/events/1"), false));
        System.out.println(stats);

    }
}


