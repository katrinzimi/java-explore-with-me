package ru.practicum.explorewithme.server.dto.mapper;

import ru.practicum.explorewithme.server.dto.event.EventFullDto;
import ru.practicum.explorewithme.server.dto.event.EventShortDto;
import ru.practicum.explorewithme.server.dto.event.UpdateEventAdminRequest;
import ru.practicum.explorewithme.server.model.Category;
import ru.practicum.explorewithme.server.model.Event;
import ru.practicum.explorewithme.server.model.enums.EventState;

public class EventMapper {
    public static EventShortDto toEventShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .paid(event.isPaid())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .eventDate(event.getEventDate())
                .category(null)
                .confirmedRequests(event.getConfirmedRequests())
                .initiator(null)
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
                .category(null)
                .confirmedRequests(event.getConfirmedRequests())
                .initiator(null)
                .views(event.getViews())
                .build();
    }

    public static Event toCompilationUpdate(UpdateEventAdminRequest updateEventAdminRequest, Event event) {
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
}
