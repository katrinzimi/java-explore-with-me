package ru.practicum.explorewithme.server.privateAPI.servise;

import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.server.dto.event.ParticipationRequestDto;

import java.util.List;
@Service
public class PrivateRequestServiceImpl implements PrivateRequestService{
    @Override
    public List<ParticipationRequestDto> getRequests(Long userId) {
        return null;
    }

    @Override
    public ParticipationRequestDto create(Long userId, Long eventId) {
        return null;
    }

    @Override
    public ParticipationRequestDto update(Long userId, Long requestsId) {
        return null;
    }
}
