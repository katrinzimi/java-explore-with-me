package ru.practicum.explorewithme.server.user.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.server.user.service.AdminUserService;
import ru.practicum.explorewithme.server.user.dto.NewUserRequest;
import ru.practicum.explorewithme.server.user.dto.UserDto;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class AdminUsersController {

    private final AdminUserService service;

    @SuppressWarnings("checkstyle:WhitespaceAround")
    @GetMapping()
    public List<UserDto> getAll(@RequestParam(required = false) List<Long> ids,
                                @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Получение списка пользователей ");
        PageRequest request = PageRequest.of(from / size, size,
                Sort.by(Sort.Direction.ASC, "id"));
        return service.getAll(ids, request);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto save(@RequestBody @Valid NewUserRequest newUserRequest) {
        log.info("Создание нового поьзователя: {}", newUserRequest.getName());
        return service.save(newUserRequest);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long userId) {
        log.info("Удаение пользователя с id {}", userId);
        service.delete(userId);
    }
}
