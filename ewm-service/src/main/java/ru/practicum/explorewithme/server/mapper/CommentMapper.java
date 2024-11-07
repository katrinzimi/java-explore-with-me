package ru.practicum.explorewithme.server.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.server.event.dto.comment.CommentDto;
import ru.practicum.explorewithme.server.event.dto.comment.CommentNewDto;
import ru.practicum.explorewithme.server.event.dto.comment.CommentUpdateDto;
import ru.practicum.explorewithme.server.model.Comment;
import ru.practicum.explorewithme.server.model.Event;
import ru.practicum.explorewithme.server.model.User;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentMapper {
    public static Comment toComment(Long eventId, CommentNewDto dto) {
        return Comment.builder()
                .user(User.builder().id(dto.getUserId()).build())
                .event(Event.builder().id(eventId).build())
                .comment(dto.getComment())
                .build();
    }

    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .userId(comment.getUser().getId())
                .eventId(comment.getEvent().getId())
                .comment(comment.getComment())
                .build();
    }

    public static Comment toComment(Comment comment, CommentUpdateDto dto) {
        comment.setComment(dto.getComment());
        return comment;
    }

    public static List<CommentDto> commentDtoList(List<Comment> comments) {
        return comments.stream().map(CommentMapper::toCommentDto).collect(Collectors.toList());
    }
}
