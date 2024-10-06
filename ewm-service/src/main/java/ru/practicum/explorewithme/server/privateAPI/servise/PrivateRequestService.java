package ru.practicum.explorewithme.server.privateAPI.servise;

import ru.practicum.explorewithme.server.dto.event.ParticipationRequestDto;

import java.util.List;

public interface PrivateRequestService {
    List<ParticipationRequestDto> getRequests(Long userId);

    ParticipationRequestDto create(Long userId, Long eventId);

    ParticipationRequestDto update(Long userId, Long requestsId);
}
