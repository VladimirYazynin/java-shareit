package ru.practicum.shareit.user;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {

    User create(User user);

    User update(User user);

    void delete(Long id);

    Optional<User> find(Long id);

    Collection<User> getAll();

}
