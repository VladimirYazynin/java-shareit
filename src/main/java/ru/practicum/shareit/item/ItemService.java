package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.Collection;

public interface ItemService {

    ItemDto findById(Long itemId);

    ItemDto createItem(Long userId, ItemCreateDto newItem);

    ItemDto updateItem(Long userId, Long itemId, ItemUpdateDto updateItem);

    Collection<ItemDto> findUserItems(Long userId);

    Collection<ItemDto> findItemsByText(String text);

}
