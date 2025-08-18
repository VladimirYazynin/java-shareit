package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@RequestBody UserCreateDto newUser) {
        log.info("Получен запрос на добавление пользователя: {}", newUser);
        return userService.create(newUser);
    }

    @PutMapping
    public UserDto update(@RequestBody UserUpdateDto updateUser) {
        log.info("Получен запрос на обновление пользователя с id: {}", updateUser.getId());
        return userService.update(updateUser);
    }

    @DeleteMapping("/userId")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long userId) {
        log.info("Получен запрос на удаление пользователя с id: {}", userId);
        userService.delete(userId);
    }

    @GetMapping
    public Collection<UserDto> findAll() {
        return userService.findAll();
    }

    @GetMapping("/userId")
    public UserDto findById(@PathVariable Long userId) {
        return userService.findById(userId);
    }

}
