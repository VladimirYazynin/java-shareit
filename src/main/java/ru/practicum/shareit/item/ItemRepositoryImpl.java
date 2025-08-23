package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Repository
public class ItemRepositoryImpl implements ItemRepository {

    private final Map<Long, Item> items = new HashMap<>();
    private Long generatorId = 1L;

    @Override
    public Optional<Item> find(Long id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public Item create(Item item) {
        item.setId(generateId());
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item update(Item item) {
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Collection<Item> findUserItems(Long userId) {
        return items.values().stream()
                .filter(item -> userId.equals(item.getOwner()))
                .toList();
    }

    @Override
    public Collection<Item> findItemsByText(String text) {
        return items.values().stream()
                .filter(Objects::nonNull)
                .filter(item -> item.getAvailable().equals(true))
                .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase()) ||
                        item.getDescription().toLowerCase().contains(text.toLowerCase()))
                .toList();
    }

    private Long generateId() {
        return generatorId++;
    }

}
