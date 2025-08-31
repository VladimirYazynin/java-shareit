package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.Item;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemMapper {

    public static Item mapToItem(ItemCreateDto newItem) {
        return Item.builder()
                .name(newItem.getName())
                .description(newItem.getDescription())
                .available(newItem.getAvailable())
                .requestId(newItem.getRequestId())
                .build();
    }

    public static Item mapToItem(ItemUpdateDto updateItem) {
        return Item.builder()
                .name(updateItem.getName())
                .description(updateItem.getDescription())
                .available(updateItem.getAvailable())
                .build();
    }

    public static ItemDto mapToItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(item.getOwner().getId())
                .build();
    }

    public static ItemExtendedDto mapToItemExtendedDto(Item item) {
        return ItemExtendedDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(item.getOwner().getId())
                .build();
    }

    public static ItemResponseDto mapToItemResponseDto(Item item) {
        return ItemResponseDto.builder()
                .id(item.getId())
                .name(item.getName())
                .ownerId(item.getOwner().getId())
                .build();
    }

}
