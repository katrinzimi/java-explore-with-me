package ru.practicum.explorewithme.server.event.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.explorewithme.server.event.dto.comment.CommentDto;
import ru.practicum.explorewithme.server.event.dto.comment.CommentNewDto;
import ru.practicum.explorewithme.server.event.dto.comment.CommentUpdateDto;

import java.util.List;

public interface CommentService {

    CommentDto save(Long eventId, CommentNewDto dto);

    CommentDto update(Long eventId, Long commentId, CommentUpdateDto dto);

    List<CommentDto> getAllComments(Long eventId, Pageable request);

    List<CommentDto> getAllCommentsByUserId(Long userId, PageRequest request);

    void delete(Long userId, Long commentId);
}
