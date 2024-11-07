package ru.practicum.explorewithme.server.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.server.event.dto.comment.CommentDto;
import ru.practicum.explorewithme.server.event.dto.comment.CommentNewDto;
import ru.practicum.explorewithme.server.event.dto.comment.CommentUpdateDto;
import ru.practicum.explorewithme.server.exception.NotFoundException;
import ru.practicum.explorewithme.server.exception.ValidationException;
import ru.practicum.explorewithme.server.mapper.CommentMapper;
import ru.practicum.explorewithme.server.model.Comment;
import ru.practicum.explorewithme.server.model.User;
import ru.practicum.explorewithme.server.repository.CommentRepository;
import ru.practicum.explorewithme.server.repository.EventRepository;
import ru.practicum.explorewithme.server.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Override
    public CommentDto save(Long eventId, CommentNewDto dto) {
        eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Данного события не существует"));
        return CommentMapper.toCommentDto(commentRepository.save(CommentMapper.toComment(eventId, dto)));
    }

    @Override
    public CommentDto update(Long eventId, Long commentId, CommentUpdateDto dto) {
        eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Данного события не существует"));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Данного комментария не найдено"));
        if (!dto.getUserId().equals(comment.getUser().getId())) {
            throw new ValidationException("Данный пользователь не может изменить комментарий");
        }
        return CommentMapper.toCommentDto(commentRepository.save(CommentMapper.toComment(comment, dto)));
    }

    @Override
    public List<CommentDto> getAllComments(Long eventId, Pageable request) {
        eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Данного события не существует"));
        List<Comment> allByEventId = commentRepository.findAllByEventId(eventId, request);
        return CommentMapper.commentDtoList(allByEventId);
    }

    @Override
    public List<CommentDto> getAllCommentsByUserId(Long userId, PageRequest request) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Данного пользователя не существует"));
        List<Comment> allByUserId = commentRepository.findAllByUserId(userId, request);
        return CommentMapper.commentDtoList(allByUserId);
    }

    @Override
    public void deleteComment(Long userId, Long commentId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Данного пользователя не существует"));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Данного комментария не найдено"));
        if (!comment.getUser().equals(user)) {
            throw new ValidationException("Данный пользователь не может удалить комментарий");
        }
        commentRepository.deleteById(commentId);
    }

    @Override
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}

