package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserUpdateDto {

    @NotNull
    private Long id;
    private String name;
    private String email;

}
