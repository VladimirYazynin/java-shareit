package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ItemResponseDto {

    private Long id;
    private String name;
    private Long ownerId;

}
