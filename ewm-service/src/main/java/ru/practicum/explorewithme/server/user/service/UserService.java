package ru.practicum.explorewithme.server.user.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.explorewithme.server.user.dto.NewUserRequest;
import ru.practicum.explorewithme.server.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAll(List<Long> ids, Pageable request);

    UserDto save(NewUserRequest newUserRequest);

    void delete(Long userId);
}
