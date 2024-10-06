package ru.practicum.explorewithme.server.adminAPI.servise.user;

import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.server.dto.user.NewUserRequest;
import ru.practicum.explorewithme.server.dto.user.UserDto;

import java.util.List;
@Service
public class AdminUserServiceImpl implements AdminUserService{
    @Override
    public List<UserDto> getAll(List<Integer> ids, Integer from, Integer size) {
        return null;
    }

    @Override
    public UserDto save(NewUserRequest newUserRequest) {
        return null;
    }

    @Override
    public void delete(Long userId) {

    }
}
