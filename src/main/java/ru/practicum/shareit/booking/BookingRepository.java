package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerIdAndStatus(Long bookerId, BookingStatus status);

    List<Booking> findAllByBookerId(Long bookerId);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = :bookerId AND b.start < CURRENT_TIMESTAMP AND b.end > CURRENT_TIMESTAMP " +
            "AND b.status = :status")
    List<Booking> findAllByBookerCurrentBookings(Long bookerId, BookingStatus status);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = :bookerId AND b.start > CURRENT_TIMESTAMP " +
            "AND b.status = :status")
    List<Booking> findAllByBookerFutureBookings(Long bookerId, BookingStatus status);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = :bookerId AND b.end < CURRENT_TIMESTAMP " +
            "AND b.status = :status")
    List<Booking> findAllByBookerPastBookings(Long bookerId, BookingStatus status);

    List<Booking> findAllByItemOwnerId(Long ownerId);

    List<Booking> findAllByItemOwnerIdAndStatus(Long ownerId, BookingStatus status);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.owner.id = :ownerId AND b.start < CURRENT_TIMESTAMP AND b.end > CURRENT_TIMESTAMP " +
            "AND b.status = :status")
    List<Booking> findAllByItemOwnerCurrentBookings(Long ownerId, BookingStatus status);

    Optional<Booking> findFirstByItemIdAndEndBeforeAndStatusOrderByEndDesc(
            Long itemId,
            LocalDateTime endBefore,
            BookingStatus status
    );

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.owner.id = :ownerId AND b.start > CURRENT_TIMESTAMP " +
            "AND b.status = :status")
    List<Booking> findAllByItemOwnerFutureBookings(Long ownerId, BookingStatus status);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.owner.id = :ownerId AND b.end < CURRENT_TIMESTAMP " +
            "AND b.status = :status")
    List<Booking> findAllByItemOwnerPastBookings(Long ownerId, BookingStatus status);

    Optional<Booking> findFirstByItemIdAndStartAfterAndStatusOrderByStartAsc(
            Long itemId,
            LocalDateTime startAfter,
            BookingStatus status
    );

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = :bookerId AND b.end < CURRENT_TIMESTAMP " +
            "AND b.item.id = :itemId " +
            "AND b.status = 'APPROVED'")
    Optional<Booking> findByBookerAndItemIdPastBooking(Long bookerId, Long itemId);

}
