package ru.practicum.explorewithme.server.user.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.server.mapper.UserMapper;
import ru.practicum.explorewithme.server.user.dto.NewUserRequest;
import ru.practicum.explorewithme.server.user.dto.UserDto;
import ru.practicum.explorewithme.server.exception.ConflictException;
import ru.practicum.explorewithme.server.model.User;
import ru.practicum.explorewithme.server.repository.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {
    private final UserRepository repository;

    @Override
    @Transactional
    public List<UserDto> getAll(List<Long> ids, Pageable request) {
        if (ids == null) {
            return UserMapper.toUserDtoList(repository.findAll(request)
                    .getContent());
        }
        List<User> allById = repository.findAllById(ids);
        return UserMapper.toUserDtoList(allById);
//        return repository.findAllById(ids).stream()
//                .map(UserMapper::toUserDto)
//                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDto save(NewUserRequest newUserRequest) {
        if (repository.existsByEmail(newUserRequest.getEmail())) {
            throw new ConflictException("Емаил уже используется");
        }
        return UserMapper.toUserDto(repository.save(UserMapper.toUser(newUserRequest)));
    }

    @Override
    @Transactional
    public void delete(Long userId) {
        repository.deleteById(userId);
    }
}
