package ru.practicum.explorewithme.server.mapper;

import ru.practicum.explorewithme.server.user.dto.NewUserRequest;
import ru.practicum.explorewithme.server.user.dto.UserDto;
import ru.practicum.explorewithme.server.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {
    public static User toUser(NewUserRequest newUserRequest) {
        return User.builder()
                .name(newUserRequest.getName())
                .email(newUserRequest.getEmail())
                .build();
    }

    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static List<UserDto> toUserDtoList(List<User> ids) {
        return ids.stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }
}
