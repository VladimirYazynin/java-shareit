package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto findById(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Не найдена вещь с id: " + itemId));
        return ItemMapper.mapToItemDto(item);
    }

    @Override
    public ItemDto createItem(Long userId, ItemCreateDto newItem) {
        User owner = userRepository.findById(userId)
                        .orElseThrow(() -> new NotFoundException("Не найден пользователь с id: " + userId));
        Item item = ItemMapper.mapToItem(newItem);
        item.setOwner(owner);
        return ItemMapper.mapToItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemUpdateDto updateItem) {
        Item existingItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Не найдена вещь с id: " + itemId));
        if (!existingItem.getOwner().equals(userId)) {
            throw new ForbiddenException("У вас нет прав для изменения этой вещи");
        }
        if (updateItem.getName() != null) {
            existingItem.setName(updateItem.getName());
        }
        if (updateItem.getDescription() != null) {
            existingItem.setDescription(updateItem.getDescription());
        }
        if (updateItem.getAvailable() != null) {
            existingItem.setAvailable(updateItem.getAvailable());
        }
        return ItemMapper.mapToItemDto(itemRepository.save(existingItem));
    }

    @Override
    public Collection<ItemDto> findUserItems(Long userId) {
        return itemRepository.findByOwnerId(userId).stream()
                .map(ItemMapper::mapToItemDto)
                .toList();
    }

    @Override
    public Collection<ItemDto> findItemsByText(String text) {
        if (text == null || text.isEmpty()) {
            return List.of();
        }
        return itemRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(text, text).stream()
                .map(ItemMapper::mapToItemDto)
                .toList();
    }

}
