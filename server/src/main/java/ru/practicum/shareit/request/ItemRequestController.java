package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestExtendedDto;

import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestDto addItemRequest(@RequestHeader("X-Sharer-User-Id") Long requestorId,
                                         @RequestBody ItemRequestCreateDto newItemRequest) {
        log.info(
                "Получен запрос на добавление \"запроса на вещь\", от пользователя с id: {}, и опсанием: {}",
                requestorId, newItemRequest.getDescription()
        );
        ItemRequestDto itemRequestDto = itemRequestService.addItemRequest(requestorId, newItemRequest);
        log.info("Успешно создан запрос: {}", itemRequestDto);
        return itemRequestDto;
    }

    @GetMapping
    public Collection<ItemRequestExtendedDto> getUserItemRequests(@RequestHeader("X-Sharer-User-Id") Long requestorId) {
        return itemRequestService.getUserItemRequests(requestorId);
    }

    @GetMapping("/all")
    public Collection<ItemRequestDto> getItemRequests() {
        return itemRequestService.getItemRequests();
    }

    @GetMapping("/{requestId}")
    public ItemRequestExtendedDto getItemRequest(@PathVariable(name = "requestId") Long requestId) {
        return itemRequestService.getItemRequest(requestId);
    }

}
