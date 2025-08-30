package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemExtendedDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.Collection;

public interface ItemService {

    ItemExtendedDto findById(Long itemId);

    ItemDto createItem(Long userId, ItemCreateDto newItem);

    ItemDto updateItem(Long userId, Long itemId, ItemUpdateDto updateItem);

    Collection<ItemExtendedDto> findUserItems(Long userId);

    Collection<ItemDto> findItemsByText(String text);

    CommentDto addComment(Long userId, Long itemId, CommentCreateDto newComment);

}
