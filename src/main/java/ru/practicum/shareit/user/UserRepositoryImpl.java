package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    @Override
    public User create(User user) {
        return null;
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public Optional<User> find(Long id) {
        return Optional.empty();
    }

    @Override
    public Collection<User> getAll() {
        return null;
    }
}
