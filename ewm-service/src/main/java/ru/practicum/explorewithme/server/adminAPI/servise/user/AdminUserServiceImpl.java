package ru.practicum.explorewithme.server.adminAPI.servise.user;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.server.dto.mapper.UserMapper;
import ru.practicum.explorewithme.server.dto.user.NewUserRequest;
import ru.practicum.explorewithme.server.dto.user.UserDto;
import ru.practicum.explorewithme.server.exception.ConflictException;
import ru.practicum.explorewithme.server.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {
    private final UserRepository repository;

    @Override
    public List<UserDto> getAll(List<Long> ids, Integer from, Integer size) {
        if (ids == null) {
            return repository.findAll(PageRequest.of(from / size, size,
                            Sort.by(Sort.Direction.ASC, "id")))
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
        if (repository.existsByEmail(newUserRequest.getEmail())) {
            throw new ConflictException("Емаил уже используется");
        }
        return UserMapper.toUserDto(repository.save(UserMapper.toUser(newUserRequest)));
    }

    @Override
    public void delete(Long userId) {
        repository.deleteById(userId);
    }
}
