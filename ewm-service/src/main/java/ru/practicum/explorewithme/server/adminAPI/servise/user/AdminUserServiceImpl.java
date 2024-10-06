package ru.practicum.explorewithme.server.adminAPI.servise.user;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.server.dto.mapper.UserMapper;
import ru.practicum.explorewithme.server.dto.user.NewUserRequest;
import ru.practicum.explorewithme.server.dto.user.UserDto;
import ru.practicum.explorewithme.server.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminUserServiceImpl implements AdminUserService {
    UserRepository repository;

    @Override
    public List<UserDto> getAll(List<Long> ids, Integer from, Integer size) {
        if (ids.isEmpty()) {
            return repository.findAll(PageRequest.of(from / size, size))
                    .getContent()
                    .stream()
                    .map(UserMapper::toUserDto)
                    .collect(Collectors.toList());
        }
        return repository.findAllById(ids).stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto save(NewUserRequest newUserRequest) {
        return UserMapper.toUserDto(repository.save(UserMapper.toUser(newUserRequest)));
    }

    @Override
    public void delete(Long userId) {
        repository.deleteById(userId);
    }
}
