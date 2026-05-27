package org.example.pensionatkademina.service;

import org.example.pensionatkademina.dto.BookingDto;
import org.example.pensionatkademina.dto.RoomDetailedDto;
import org.example.pensionatkademina.model.Booking;
import org.example.pensionatkademina.model.Room;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {
        List<BookingDto> getAllBookings();
        BookingDto getBookingById(Long id);
        BookingDto createBooking(BookingDto bookingDto);
        BookingDto updateBooking(Long id, BookingDto bookingDto);
        void deleteBooking(Long id);
        List<RoomDetailedDto> searchAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate,
                                                          int numberOfGuests) ;
}
