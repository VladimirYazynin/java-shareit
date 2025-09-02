package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestExtendedDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public ItemRequestDto addItemRequest(Long requestorId, ItemRequestCreateDto newItemRequest) {
        User requestor = userRepository.findById(requestorId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с id: " + requestorId));
        ItemRequest itemRequest = ItemRequest.builder()
                .description(newItemRequest.getDescription())
                .requestor(requestor)
                .created(LocalDateTime.now())
                .build();
        return ItemRequestMapper.mapToItemRequestDto(itemRequestRepository.save(itemRequest));
    }

    @Override
    public Collection<ItemRequestExtendedDto> getUserItemRequests(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с id: " + userId));

        List<ItemRequest> userItemRequests =
                itemRequestRepository.findAllByRequestorIdOrderByCreatedDesc(userId);

        List<Long> itemRequestIds = userItemRequests.stream()
                .map(ItemRequest::getId)
                .collect(Collectors.toList());

        List<Item> items = itemRepository.findAllByRequestIdIn(itemRequestIds);

        Map<Long, List<Item>> itemsByRequestId = items.stream()
                .collect(Collectors.groupingBy(item -> item.getRequest().getId()));

        return userItemRequests.stream()
                .map(itemRequest -> ItemRequestMapper.mapToItemRequestExtendedDto(
                        itemRequest,
                        itemsByRequestId.get(itemRequest.getId())
                ))
                .toList();
    }

    @Override
    public Collection<ItemRequestDto> getItemRequests() {
        return itemRequestRepository.findAllByOrderByCreatedDesc().stream()
                .map(ItemRequestMapper::mapToItemRequestDto)
                .toList();
    }

    @Override
    public ItemRequestExtendedDto getItemRequest(Long requestId) {
        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Не найден запрос на добавление вещи с id: " + requestId));
        List<Item> items = itemRepository.findAllByRequestId(requestId);

        return ItemRequestMapper.mapToItemRequestExtendedDto(itemRequest, items);
    }

}
