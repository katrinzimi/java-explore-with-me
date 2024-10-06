package ru.practicum.explorewithme.server.adminAPI.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.server.adminAPI.servise.user.AdminUserService;
import ru.practicum.explorewithme.server.dto.user.NewUserRequest;
import ru.practicum.explorewithme.server.dto.user.UserDto;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class AdminUsersController {

    private final AdminUserService service;

    @GetMapping()
    public List<UserDto> getAll(@RequestParam(required = false) List<Long> ids,
                                @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Получение списка пользователей ");
        return service.getAll(ids, from, size);
    }

    @PostMapping()
    public UserDto save(@RequestBody @Valid NewUserRequest newUserRequest) {
        log.info("Создание нового поьзователя: {}", newUserRequest.getName());
        return service.save(newUserRequest);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable Long userId) {
        log.info("Удаение пользователя с id {}", userId);
        service.delete(userId);
    }
}
