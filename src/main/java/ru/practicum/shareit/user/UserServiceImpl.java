package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicateException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Collection<UserDto> findAll() {
        return userRepository.getAll().stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }

    @Override
    public UserDto findById(Long userId) {
        User user = userRepository.find(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с id: " + userId));
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public UserDto create(UserCreateDto newUser) {
        User user = UserMapper.mapToUser(newUser);
        if (isEmailRegistered(newUser.getEmail())) {
            throw new DuplicateException("Этот email уже занят");
        }
        return UserMapper.mapToUserDto(userRepository.create(user));
    }

    @Override
    public UserDto update(Long userId, UserUpdateDto updateUser) {
        User existingUser = userRepository.find(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с id: " + userId));
        updateUser.setId(userId);
//        User modifiedUser = UserMapper.mapToUser(updateUser);
        if (updateUser.getEmail() != null) {
            if (!existingUser.getEmail().equals(updateUser.getEmail())) {
                if (isEmailRegistered(updateUser.getEmail())) {
                    throw new DuplicateException("Этот email уже занят");
                }
                existingUser.setEmail(updateUser.getEmail());
            }
        }
        if (updateUser.getName() != null) {
            existingUser.setName(updateUser.getName());
        }

        userRepository.update(existingUser);
        return UserMapper.mapToUserDto(existingUser);
    }

    @Override
    public void delete(Long id) {
        userRepository.delete(id);
    }

    private boolean isEmailRegistered(String email) {
        return userRepository.getAll().stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }
}
