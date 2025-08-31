package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.request.ItemRequest;

import java.util.List;

public class ItemRequestMapper {

    public static ItemRequestDto mapToItemRequestDto(ItemRequest itemRequest) {
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .requestorId(itemRequest.getRequestor().getId())
                .build();
    }

    public static ItemRequestExtendedDto mapToItemRequestExtendedDto(ItemRequest itemRequest, List<Item> items) {
        List<ItemResponseDto> itemsDto;
        if (items == null) {
            itemsDto = List.of();
        } else {
            itemsDto = items.stream()
                    .map(ItemMapper::mapToItemResponseDto)
                    .toList();
        }
        return ItemRequestExtendedDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .requestorId(itemRequest.getRequestor().getId())
                .items(itemsDto)
                .build();
    }

}
