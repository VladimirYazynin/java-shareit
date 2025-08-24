package ru.practicum.shareit.item;

import java.util.Collection;
import java.util.Optional;

public interface ItemRepository {

    Optional<Item> find(Long id);

    Item create(Item item);

    Item update(Item item);

    Collection<Item> findUserItems(Long userId);

    Collection<Item> findItemsByText(String text);

}
