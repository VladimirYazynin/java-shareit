package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> addItemRequest(@RequestHeader("X-Sharer-User-Id") Long requestorId,
                                                 @Valid @RequestBody ItemRequestCreateDto newItemRequest) {
        log.info(
                "Получен запрос на добавление \"запроса на вещь\", от пользователя с id: {}, и опсанием: {}",
                requestorId, newItemRequest.getDescription()
        );
        return itemRequestClient.addItemRequest(requestorId, newItemRequest);
    }

    @GetMapping
    public ResponseEntity<Object> getUserItemRequests(@RequestHeader("X-Sharer-User-Id") Long requestorId) {
        return itemRequestClient.getUserItemRequests(requestorId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getItemRequests() {
        return itemRequestClient.getItemRequests();
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequest(@PathVariable(name = "requestId") Long requestId) {
        return itemRequestClient.getItemRequest(requestId);
    }

}
