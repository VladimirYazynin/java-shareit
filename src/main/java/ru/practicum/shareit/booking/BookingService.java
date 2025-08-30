package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.Collection;

public interface BookingService {

    BookingDto addBooking(Long bookerId, BookingCreateDto newBooking);

    BookingDto updateBookingStatus(Long ownerId, Long bookingId, Boolean approved);

    BookingDto getBookingDetails(Long userId, Long bookingId);

    Collection<BookingDto> getUserBookings(Long userId, BookingState state);

    Collection<BookingDto> getOwnerItemsBookings(Long ownerId, BookingState state);

}
