package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Collection<UserDto> findAll() {
        userRepository.getAll();

        return null;
    }

    @Override
    public UserDto findById(Long userId) {
        return null;
    }

    @Override
    public UserDto create(UserCreateDto newUser) {
        return null;
    }

    @Override
    public UserDto update(UserUpdateDto updateUser) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
