package ru.practicum.explorewithme.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.domain.Page;
import ru.practicum.explorewithme.server.model.Category;
import ru.practicum.explorewithme.server.model.Event;
import ru.practicum.explorewithme.server.model.User;
import ru.practicum.explorewithme.server.publicAPI.dto.RequestParamEvent;
import ru.practicum.explorewithme.server.publicAPI.dto.RequestParamEventSort;
import ru.practicum.explorewithme.server.repository.CategoriesRepository;
import ru.practicum.explorewithme.server.repository.EventRepository;
import ru.practicum.explorewithme.server.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
//@ComponentScan("ru.practicum.explorewithme")
public class Main {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Main.class, args);
        EventRepository repository = context.getBean(EventRepository.class);
        UserRepository userRepository = context.getBean(UserRepository.class);
        CategoriesRepository categoriesRepository = context.getBean(CategoriesRepository.class);
        userRepository.save(new User(1L, "Wenya", "wenya@mail.ru"));
        categoriesRepository.save(new Category(1L, "kino"));
        repository.save(new Event(1, "papapap", "qwerty",
                categoriesRepository.findById(1L).orElseThrow(), LocalDateTime.now(),10,
                true, 2, userRepository.findById(1L).orElseThrow(), 1));
        List<Event> all = repository.findAll();
        System.out.println(all);
        RequestParamEvent requestParamEvent = RequestParamEvent.builder()
                .text("qwert")
                .paid(true)
                .categories(List.of(1L))
                .onlyAvailable(true)
                .rangeStart(LocalDateTime.now().minusDays(1))
                .rangeEnd(LocalDateTime.now())
                .from(0)
                .size(10)
                .sort(RequestParamEventSort.EVENT_DATE)
                .build();
        Page<Event> result = repository.findAllByCriteria(requestParamEvent);
        System.out.println(result.getContent());

    }
}


