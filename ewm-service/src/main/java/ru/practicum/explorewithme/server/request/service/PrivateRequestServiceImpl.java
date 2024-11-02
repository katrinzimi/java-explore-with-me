package ru.practicum.explorewithme.server.request.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.server.exception.NotFoundException;
import ru.practicum.explorewithme.server.event.dto.ParticipationRequestDto;
import ru.practicum.explorewithme.server.mapper.RequestMapper;
import ru.practicum.explorewithme.server.exception.ConflictException;
import ru.practicum.explorewithme.server.exception.ValidationException;
import ru.practicum.explorewithme.server.model.Event;
import ru.practicum.explorewithme.server.model.Request;
import ru.practicum.explorewithme.server.model.User;
import ru.practicum.explorewithme.server.model.enums.EventState;
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
    @Transactional
    public List<ParticipationRequestDto> getRequests(Long userId) {
        userRepository.findById(userId).orElseThrow();
        return RequestMapper.toDtoList(requestRepository.findAllByRequesterId(userId));
    }

    @Override
    @Transactional
    public ParticipationRequestDto create(Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользоватеь не найден"));
        if (eventId == null) {
            throw new ValidationException("id события отсутствует");
        }
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Событие не найдено"));
        if (requestRepository.existsByRequesterIdAndEventId(userId, eventId)) {
            throw new ConflictException("Нельзя добавить повторный запрос");
        }
        if (userId.equals(event.getInitiator().getId())) {
            throw new ConflictException("Инициатор события не может добавить запрос на участие в своём событии");
        }
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Нельзя участвовать в неопубликованном событии");
        }
        if (event.getParticipantLimit() != 0 &&
                event.getParticipantLimit() == (requestRepository.countByEventIdAndStatus(eventId, RequestState.CONFIRMED))) {
            throw new ConflictException("Количество участников в событии достигло предела");
        }
        Request request = Request.builder()
                .requester(user)
                .created(LocalDateTime.now())
                .event(event)
                .build();
        if (canConfirmAllRequests(event)) {
            request.setStatus(RequestState.CONFIRMED);
        } else {
            request.setStatus(RequestState.PENDING);
        }
        return RequestMapper.participationRequestDto(requestRepository.save(request));
    }

    @Override
    @Transactional
    public ParticipationRequestDto update(Long userId, Long requestsId) {
        Request request = requestRepository.findByIdAndRequesterId(requestsId, userId);
        ParticipationRequestDto participationRequestDto = RequestMapper.participationRequestDto(requestRepository.save(request));
        participationRequestDto.setStatus(RequestState.CANCELED);
        return participationRequestDto;
    }

    private boolean canConfirmAllRequests(Event event) {
        return !event.isRequestModeration() || event.getParticipantLimit() == 0;
    }
}
