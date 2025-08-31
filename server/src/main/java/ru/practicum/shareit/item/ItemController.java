package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemExtendedDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody ItemCreateDto newItem) {
        log.info("Получен запрос от пользователя с id: {}, на добавление вещи: {}", userId, newItem);
        ItemDto itemDto = itemService.createItem(userId, newItem);
        log.info("Добавлена вещь: {}", itemDto);
        return itemDto;
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") Long userId,
                          @PathVariable Long itemId,
                          @RequestBody ItemUpdateDto updateItem) {
        log.info("Получен запрос от пользователя с id: {}, на обновление вещи с id: {}", userId, itemId);
        ItemDto itemDto = itemService.updateItem(userId, itemId, updateItem);
        log.info("Данные о вещи обновлены: {}", itemDto);
        return itemDto;
    }

    @GetMapping("/{itemId}")
    public ItemExtendedDto findById(@PathVariable Long itemId) {
        return itemService.findById(itemId);
    }

    @GetMapping
    public Collection<ItemExtendedDto> findUserItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.findUserItems(userId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> findItemsByText(@RequestParam(name = "text", required = false) String text) {
        return itemService.findItemsByText(text);
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @PathVariable Long itemId,
                                 @RequestBody @Valid CommentCreateDto newComment) {
        return itemService.addComment(userId, itemId, newComment);
    }

}
