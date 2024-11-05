package ru.practicum.explorewithme.server.request.service;

import ru.practicum.explorewithme.server.event.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    List<ParticipationRequestDto> getRequests(Long userId);

    ParticipationRequestDto create(Long userId, Long eventId);

    ParticipationRequestDto update(Long userId, Long requestsId);
}
