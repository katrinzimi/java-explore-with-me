package ru.practicum.explorewithme.server.dto.mapper;

import ru.practicum.explorewithme.server.dto.category.CategoryDto;
import ru.practicum.explorewithme.server.dto.event.*;
import ru.practicum.explorewithme.server.dto.user.UserShortDto;
import ru.practicum.explorewithme.server.model.Category;
import ru.practicum.explorewithme.server.model.Event;
import ru.practicum.explorewithme.server.model.User;
import ru.practicum.explorewithme.server.model.enums.EventState;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EventMapper {
    public static EventShortDto toEventShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .paid(event.isPaid())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .eventDate(event.getEventDate())
                .category(CategoryDto.builder().id(event.getCategory().getId()).build())
                .confirmedRequests(event.getConfirmedRequests())
                .initiator(UserShortDto.builder().id(event.getInitiator().getId()).build())
                .views(event.getViews())
                .build();
    }

    public static EventFullDto toEventFullDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .paid(event.isPaid())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .eventDate(event.getEventDate())
                .category(CategoryDto.builder().id(event.getCategory().getId()).build())
                .confirmedRequests(event.getConfirmedRequests())
                .initiator(UserShortDto.builder().id(event.getInitiator().getId()).build())
                .views(event.getViews())
                .build();
    }

    public static Event toEvent(NewEventDto eventDto, User user) {
        return Event.builder()
                .paid(eventDto.isPaid())
                .title(eventDto.getTitle())
                .annotation(eventDto.getAnnotation())
                .eventDate(eventDto.getEventDate())
                .category(Category.builder().id(eventDto.getCategory()).build())
                .initiator(user)
                .state(EventState.PENDING)
                .build();
    }

    public static Event toEventUpdate(UpdateEventAdminRequest updateEventAdminRequest, Event event) {
        if (updateEventAdminRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventAdminRequest.getAnnotation());
        }
        if (updateEventAdminRequest.getTitle() != null) {
            event.setTitle(updateEventAdminRequest.getTitle());
        }
        if (updateEventAdminRequest.getEventDate() != null) {
            event.setEventDate(updateEventAdminRequest.getEventDate());
        }
        if (updateEventAdminRequest.getCategory() != null) {
            event.setCategory(Category.builder().id(updateEventAdminRequest.getCategory()).build());
        }
        if (updateEventAdminRequest.getPaid() != null) {
            event.setPaid(updateEventAdminRequest.getPaid());
        }
        if (updateEventAdminRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventAdminRequest.getParticipantLimit());
        }
        if (updateEventAdminRequest.getStateAction() != null) {
            if (updateEventAdminRequest.getStateAction().equals(UpdateEventAdminRequest.StateAction.PUBLISH_EVENT)) {
                event.setState(EventState.PUBLISHED);
            }
            if (updateEventAdminRequest.getStateAction().equals(UpdateEventAdminRequest.StateAction.REJECT_EVENT)) {
                event.setState(EventState.CANCELED);
            }
        }
        return event;
    }

    public static Event toEventUpdate(UpdateEventUserRequest eventUserRequest, Event event) {
        if (eventUserRequest.getAnnotation() != null) {
            event.setAnnotation(eventUserRequest.getAnnotation());
        }
        if (eventUserRequest.getTitle() != null) {
            event.setTitle(eventUserRequest.getTitle());
        }
        if (eventUserRequest.getEventDate() != null) {
            event.setEventDate(eventUserRequest.getEventDate());
        }
        if (eventUserRequest.getCategory() != null) {
            event.setCategory(Category.builder().id(eventUserRequest.getCategory()).build());
        }
        if (eventUserRequest.getPaid() != null) {
            event.setPaid(eventUserRequest.getPaid());
        }
        if (eventUserRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(eventUserRequest.getParticipantLimit());
        }
        return event;
    }

    public static Set<EventShortDto> toEventShortDtoList(List<Event> events) {
        return events.stream().map(EventMapper::toEventShortDto).collect(Collectors.toSet());
    }
}
