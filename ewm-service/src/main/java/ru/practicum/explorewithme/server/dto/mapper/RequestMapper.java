package ru.practicum.explorewithme.server.dto.mapper;

import ru.practicum.explorewithme.server.dto.event.ParticipationRequestDto;
import ru.practicum.explorewithme.server.model.Request;
import ru.practicum.explorewithme.server.model.enums.RequestState;

import java.util.List;
import java.util.stream.Collectors;

public class RequestMapper {
    public static ParticipationRequestDto participationRequestDto(Request request) {
        return ParticipationRequestDto.builder()
                .id(request.getId())
                .requester(request.getRequester().getId())
                .event(request.getEvent().getId())
                .created(request.getCreated())
                .status(request.getStatus())
                .build();
    }

    public static List<ParticipationRequestDto> toDtoList(List<Request> allByEventId) {
        return allByEventId.stream().map(RequestMapper::participationRequestDto).collect(Collectors.toList());
    }
}
