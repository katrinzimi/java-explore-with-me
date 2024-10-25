//package ru.practicum.explorewithme.server.publicAPI.controller;
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Profile;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import ru.practicum.explorewithme.server.JsonUtil;
//import ru.practicum.explorewithme.server.dto.event.EventFullDto;
//import ru.practicum.explorewithme.server.dto.event.EventShortDto;
//import ru.practicum.explorewithme.server.dto.mapper.EventMapper;
//import ru.practicum.explorewithme.server.model.Category;
//import ru.practicum.explorewithme.server.model.Event;
//import ru.practicum.explorewithme.server.model.User;
//import ru.practicum.explorewithme.server.model.enums.EventState;
//import ru.practicum.explorewithme.server.publicAPI.servise.event.PublicEventsService;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(controllers = PublicEventsController.class)
//@Profile("test")
//class PublicEventsControllerTest {
//    Event event;
//
//    @Autowired
//    private MockMvc mvc;
//
//    @MockBean
//    private PublicEventsService eventsService;
//
//    @BeforeEach
//    void setUp() {
//        event = new Event(1, "title", "annotation",
//                Category.builder().id(1L).name("kino").build(), LocalDateTime.now(),
//                5, true, 2,
//                User.builder().id(1L).name("Vanya").email("vanya@mail.ru").build(), 2, EventState.PUBLISHED);
//    }
//
//    @AfterEach
//    void tearDown() {
//    }
//
//    @Test
//    void getAllEvents() throws Exception {
//        EventShortDto eventShortDto = EventMapper.toEventShortDto(event);
//        List<EventShortDto> events = List.of(
//                eventShortDto
//        );
//        Mockito.when(eventsService.getAll(any())).thenReturn(events);
//
//        mvc.perform(get("/events")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(JsonUtil.toJson(events)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(events.size()))
//                .andExpect(jsonPath("$[0].id").value(eventShortDto.getId()))
//                .andExpect(jsonPath("$[0].title").value(eventShortDto.getTitle()))
//                .andExpect(jsonPath("$[0].annotation").value(eventShortDto.getAnnotation()));
//    }
//
//    @Test
//    void getEvent() throws Exception {
//        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
//        Mockito.when(eventsService.get(eq(1L))).thenReturn(eventFullDto);
//
//        mvc.perform(get("/events/{id}", 1L)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(JsonUtil.toJson(eventFullDto)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.title").value(eventFullDto.getTitle()))
//                .andExpect(jsonPath("$.annotation").value(eventFullDto.getAnnotation()))
//                .andExpect(jsonPath("$.paid").value(eventFullDto.isPaid()))
//                .andExpect(jsonPath("$.views").value(eventFullDto.getViews()));
//    }
//}