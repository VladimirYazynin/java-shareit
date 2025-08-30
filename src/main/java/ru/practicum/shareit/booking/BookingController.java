package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.Collection;

/**
 * TODO Sprint add-bookings.
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto addBooking(@RequestHeader("X-Sharer-User-Id") Long bookerId,
                                 @Valid @RequestBody BookingCreateDto newBooking) {
        log.info("Получен запрос на создание брони: {}", newBooking);
        BookingDto bookingDto = bookingService.addBooking(bookerId, newBooking);
        log.info("Добавлена бронь: {}", bookingDto);
        return bookingDto;
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateBookingStatus(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                    @PathVariable Long bookingId,
                                    @RequestParam(name = "approved", required = true) Boolean approved) {
        log.info("Получен запрос на обновление статуса брони с id: {}, пользователем с id: {}", bookingId, ownerId);
        BookingDto bookingDto = bookingService.updateBookingStatus(ownerId, bookingId, approved);
        log.info("Статус брони успешно обновлён: {}", bookingDto);
        return bookingDto;
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingDetails(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long bookingId) {
        return bookingService.getBookingDetails(userId, bookingId);
    }

    @GetMapping
    public Collection<BookingDto> getUserBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                  @RequestParam(name = "state", defaultValue = "ALL") BookingState state) {
        return bookingService.getUserBookings(userId, state);
    }

    @GetMapping("/owner")
    public Collection<BookingDto> getOwnerItemsBookings(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                                        @RequestParam(name = "state", defaultValue = "ALL") BookingState state) {
        return bookingService.getOwnerItemsBookings(ownerId, state);
    }
}
