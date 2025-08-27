package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.ItemNotAvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public BookingDto addBooking(Long bookerId, BookingCreateDto newBooking) {
        User booker = userRepository.findById(bookerId)
                        .orElseThrow(() -> new NotFoundException("Не найден пользователь с id: " + bookerId));
        Item bookingItem = itemRepository.findById(newBooking.getItemId())
                .orElseThrow(() -> new NotFoundException("Не найдена вещь с id: " + newBooking.getItemId()));
        if (bookingItem.getAvailable() == false) {
            throw new ItemNotAvailableException("Вещь недоступна в данный момент");
        }
        Booking booking = BookingMapper.mapToBooking(newBooking);
        booking.setBooker(booker);
        booking.setItem(bookingItem);
        booking.setStatus(BookingStatus.WAITING);
        return BookingMapper.mapToBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto updateBookingStatus(Long ownerId, Long bookingId, Boolean approved) {
//        User owner = userRepository.findById(ownerId)
//                .orElseThrow(() -> new NotFoundException("Не найден пользователь с id: " + ownerId));
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Не найдена бронь с id: " + bookingId));
        Item item = booking.getItem();
        if (!item.getOwner().getId().equals(ownerId)) {
            throw new ForbiddenException("У вас недостаточно прав для этого действия");
        }
        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
            item.setAvailable(false);
            itemRepository.save(item);
            bookingRepository.save(booking);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
            bookingRepository.save(booking);
        }

        return BookingMapper.mapToBookingDto(booking);
    }
}
