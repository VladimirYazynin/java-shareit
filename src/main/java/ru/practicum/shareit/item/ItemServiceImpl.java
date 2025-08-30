package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemExtendedDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public ItemExtendedDto findById(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Не найдена вещь с id: " + itemId));
        List<CommentDto> comments = commentRepository.findAllByItemId(itemId).stream()
                .map(CommentMapper::mapToCommentDto)
                .toList();
        ItemExtendedDto itemDto = ItemMapper.mapToItemExtendedDto(item);
        itemDto.setComments(comments);
        return itemDto;
    }

    @Override
    public ItemDto createItem(Long userId, ItemCreateDto newItem) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Не найден пользователь с id: " + userId));
        Item item = ItemMapper.mapToItem(newItem);
        item.setOwner(owner);
        return ItemMapper.mapToItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemUpdateDto updateItem) {
        Item existingItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Не найдена вещь с id: " + itemId));
        if (!existingItem.getOwner().getId().equals(userId)) {
            throw new ForbiddenException("У вас нет прав для изменения этой вещи");
        }
        if (updateItem.getName() != null) {
            existingItem.setName(updateItem.getName());
        }
        if (updateItem.getDescription() != null) {
            existingItem.setDescription(updateItem.getDescription());
        }
        if (updateItem.getAvailable() != null) {
            existingItem.setAvailable(updateItem.getAvailable());
        }
        return ItemMapper.mapToItemDto(itemRepository.save(existingItem));
    }

    @Override
    public Collection<ItemExtendedDto> findUserItems(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Не найден пользователь с id: " + userId));
        Collection<Item> userItems = itemRepository.findByOwnerId(userId);
        return userItems.stream()
                .map(ItemMapper::mapToItemExtendedDto)
                .peek(i -> {
                    setBookingsDate(i);
                    setComments(i);
                })
                .toList();
    }

    @Override
    public Collection<ItemDto> findItemsByText(String text) {
        if (text == null || text.isEmpty()) {
            return List.of();
        }
        return itemRepository.searchAvailableItemsByText(text).stream()
                .map(ItemMapper::mapToItemDto)
                .toList();
    }

    @Override
    public CommentDto addComment(Long userId, Long itemId, CommentCreateDto newComment) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Не найден пользователь с id: " + userId));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Не найдена вещь с id: " + itemId));
        bookingRepository.findByBookerAndItemIdPastBooking(userId, itemId)
                .orElseThrow(() -> new ValidationException("Вы не можете оставить свой комментарий для этой вещи"));
        Comment comment = new Comment();
        comment.setText(newComment.getText());
        comment.setAuthor(user);
        comment.setItem(item);

        return CommentMapper.mapToCommentDto(commentRepository.save(comment));
    }

    private void setBookingsDate(ItemExtendedDto extendedDto) {
        Optional<Booking> lastBooking = bookingRepository.findFirstByItemIdAndEndBeforeAndStatusOrderByEndDesc(
                extendedDto.getId(),
                LocalDateTime.now(),
                BookingStatus.APPROVED
        );
        lastBooking.ifPresent(booking -> extendedDto.setLastBooking(booking.getEnd()));

        Optional<Booking> nextBooking = bookingRepository.findFirstByItemIdAndStartAfterAndStatusOrderByStartAsc(
                extendedDto.getId(),
                LocalDateTime.now(),
                BookingStatus.APPROVED
        );
        nextBooking.ifPresent(booking -> extendedDto.setNextBooking(booking.getStart()));
    }

    private void setComments(ItemExtendedDto extendedDto) {
        List<Comment> comments = commentRepository.findAllByItemId(extendedDto.getId());
        List<CommentDto> commentsDto = comments.stream()
                .map(CommentMapper::mapToCommentDto)
                .toList();
        extendedDto.setComments(commentsDto);
    }

}
