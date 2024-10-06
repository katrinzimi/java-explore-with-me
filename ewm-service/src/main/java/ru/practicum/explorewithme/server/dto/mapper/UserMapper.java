package ru.practicum.explorewithme.server.dto.mapper;

import ru.practicum.explorewithme.server.dto.user.NewUserRequest;
import ru.practicum.explorewithme.server.dto.user.UserDto;
import ru.practicum.explorewithme.server.model.User;

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
}
