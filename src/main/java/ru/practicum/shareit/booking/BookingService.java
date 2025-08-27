package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;

public interface BookingService {

    BookingDto addBooking(Long bookerId, BookingCreateDto newBooking);

    BookingDto updateBookingStatus(Long ownerId, Long bookingId, Boolean approved);

}
