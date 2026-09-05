package org.example.pensionatkademina.repository;

import org.example.pensionatkademina.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    boolean existsByCustomerId(Long customerId);
    boolean existsByCustomerIdAndRoom_Id(Long customerId, Long roomId);
    Booking findBookingById(Long id);
    List<Booking> findBookingsByCustomerId(Long customerId);

    @Query("""
            SELECT COUNT(b) > 0
            FROM Booking b
            WHERE b.room.id = :roomId
            AND (:bookingId IS NULL OR b.id <> :bookingId)
            AND b.checkInDate < :checkOutDate
            AND b.checkOutDate > :checkInDate
            """)

    boolean roomIsBooked(Long roomId,
                         LocalDate checkInDate,
                         LocalDate checkOutDate,
                         Long bookingId);
}