package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.ItemNotAvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.Collection;
import java.util.List;

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
        if (!bookingItem.getAvailable()) {
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

    @Override
    public BookingDto getBookingDetails(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Не найдена бронь с id: " + bookingId));
        if (!booking.getBooker().getId().equals(userId)) {
            if (!booking.getItem().getOwner().getId().equals(userId)) {
                throw new ForbiddenException("У вас нет доступа к брони с id: " + bookingId);
            }
        }

        return BookingMapper.mapToBookingDto(booking);
    }

    @Override
    public List<BookingDto> getUserBookings(Long userId, BookingState state) {
        List<Booking> bookings = switch (state) {
            case ALL -> bookingRepository.findAllByBookerId(userId);
            case CURRENT -> bookingRepository.findAllByBookerCurrentBookings(userId);
            case PAST -> bookingRepository.findAllByBookerPastBookings(userId);
            case FUTURE -> bookingRepository.findAllByBookerFutureBookings(userId);
            case WAITING -> bookingRepository.findAllByBookerIdAndStatus(userId, BookingStatus.WAITING);
            case REJECTED -> bookingRepository.findAllByBookerIdAndStatus(userId, BookingStatus.REJECTED);
            default -> throw new ValidationException("Некорректный параметр статуса");
        };

        return bookings.stream()
                .map(BookingMapper::mapToBookingDto)
                .toList();
    }

    @Override
    public Collection<BookingDto> getOwnerItemsBookings(Long ownerId, BookingState state) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Не найден пользователь с id: " + ownerId));

        List<Booking> bookings = switch (state) {
            case ALL -> bookingRepository.findAllByItemOwnerId(ownerId);
            case CURRENT -> bookingRepository.findAllByItemOwnerCurrentBookings(ownerId);
            case PAST -> bookingRepository.findAllByItemOwnerPastBookings(ownerId);
            case FUTURE -> bookingRepository.findAllByItemOwnerFutureBookings(ownerId);
            case WAITING -> bookingRepository.findAllByItemOwnerIdAndStatus(ownerId, BookingStatus.WAITING);
            case REJECTED -> bookingRepository.findAllByItemOwnerIdAndStatus(ownerId, BookingStatus.REJECTED);
            default -> throw new ValidationException("Некорректный параметр статуса");
        };

        return bookings.stream()
                .map(BookingMapper::mapToBookingDto)
                .toList();
    }
}
