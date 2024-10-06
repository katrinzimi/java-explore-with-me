package ru.practicum.explorewithme.server.publicAPI.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.explorewithme.server.JsonUtil;
import ru.practicum.explorewithme.server.dto.category.CategoryDto;
import ru.practicum.explorewithme.server.dto.event.EventShortDto;
import ru.practicum.explorewithme.server.dto.user.UserShortDto;
import ru.practicum.explorewithme.server.publicAPI.servise.event.PublicEventsService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PublicEventsController.class)
@Profile("test")
class PublicEventsControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PublicEventsService eventsService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAllEvents() throws Exception {
        EventShortDto event1 = new EventShortDto(1L, "Event 1", "Cool", CategoryDto.builder().build(),
                2, LocalDateTime.now(), UserShortDto.builder().build(), false, 2);

        List<EventShortDto> events = List.of(
                event1
        );
        Mockito.when(eventsService.getAll(any())).thenReturn(events);

        mvc.perform(get("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(events)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(events.size()))
                .andExpect(jsonPath("$[0].id").value(event1.getId()))
                .andExpect(jsonPath("$[0].title").value(event1.getTitle()))
                .andExpect(jsonPath("$[0].annotation").value(event1.getAnnotation()));
    }

    @Test
    void getEvent() {
    }
}