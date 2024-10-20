package ru.practicum.explorewithme.server.privateAPI.servise.request;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.server.dto.event.ParticipationRequestDto;
import ru.practicum.explorewithme.server.dto.mapper.RequestMapper;
import ru.practicum.explorewithme.server.model.Event;
import ru.practicum.explorewithme.server.model.Request;
import ru.practicum.explorewithme.server.model.User;
import ru.practicum.explorewithme.server.model.enums.RequestState;
import ru.practicum.explorewithme.server.repository.EventRepository;
import ru.practicum.explorewithme.server.repository.RequestRepository;
import ru.practicum.explorewithme.server.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class PrivateRequestServiceImpl implements PrivateRequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public List<ParticipationRequestDto> getRequests(Long userId) {
        userRepository.findById(userId).orElseThrow();
        return RequestMapper.toDtoList(requestRepository.findAllByRequesterId(userId));
    }

    @Override
    public ParticipationRequestDto create(Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow();
        Event event = eventRepository.findById(eventId).orElseThrow();
        Request request = Request.builder()
                .requester(user)
                .created(LocalDateTime.now())
                .event(event)
                .status(RequestState.PENDING)
                .build();
        return RequestMapper.participationRequestDto(requestRepository.save(request));
    }

    @Override
    public ParticipationRequestDto update(Long userId, Long requestsId) {
        Request request = requestRepository.findByIdAndRequesterId(requestsId, userId);
        request.setStatus(RequestState.CANCELED);
        return RequestMapper.participationRequestDto(requestRepository.save(request));
    }
}
