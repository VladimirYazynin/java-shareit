package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.Collection;

public interface UserService {

    Collection<UserDto> findAll();

    UserDto findById(Long userId);

    UserDto create(UserCreateDto newUser);

    UserDto update(UserUpdateDto updateUser);

    void delete(Long id);

}
