package org.example.pensionatkademina.service;

import lombok.RequiredArgsConstructor;
import org.example.pensionatkademina.dto.BookingDto;
import org.example.pensionatkademina.dto.RoomDetailedDto;
import org.example.pensionatkademina.model.Booking;
import org.example.pensionatkademina.model.Customer;
import org.example.pensionatkademina.model.Room;
import org.example.pensionatkademina.repository.BookingRepository;
import org.example.pensionatkademina.repository.CustomerRepository;
import org.example.pensionatkademina.repository.RoomRepository;
import org.example.pensionatkademina.utility.RoomSize;
import org.example.pensionatkademina.utility.RoomType;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;
    private final RoomRepository roomRepository;

    public List<BookingDto> getAllBookings() {

        return bookingRepository.findAll()
                .stream()
                .map(this::toBookingDto)
                .toList();
    }

    public BookingDto getBookingById(Long id) {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Bokningen hittades inte!"));

        return toBookingDto(booking);
    }

    public BookingDto createBooking(BookingDto bookingDto) {

        Booking booking = new Booking();

        return saveBooking(booking, bookingDto, null);
    }

    public BookingDto updateBooking(Long id, BookingDto bookingDto) {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Bokningen hittades inte!"));

        return saveBooking(booking, bookingDto, id);
    }

    public void deleteBooking(Long id) {

        if (!bookingRepository.existsById(id)) {
            throw new IllegalArgumentException("Bokningen finns inte!");
        }

        bookingRepository.deleteById(id);
    }

    public List<RoomDetailedDto> searchAvailableRooms(LocalDate checkInDate,
                                                      LocalDate checkOutDate,
                                                      int numberOfGuests) {

        if (!checkOutDate.isAfter(checkInDate)) {
            throw new IllegalArgumentException(
                    "Utcheckning måste ske efter incheckning!");
        }

        if (numberOfGuests < 1) {
            throw new IllegalArgumentException(
                    "Antal gäster måste vara minst 1!");
        }

        return roomRepository.findAll()
                .stream()
                .filter(room -> numberOfGuests <= getMaxGuests(room))
                .filter(room -> !bookingRepository.roomIsBooked(
                        room.getId(),
                        checkInDate,
                        checkOutDate,
                        null
                ))
                .map(this::toRoomDto)
                .toList();
    }

    private BookingDto saveBooking(Booking booking,
                                   BookingDto bookingDto,
                                   Long bookingId) {

        Customer customer = customerRepository.findById(
                        bookingDto.getCustomerId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Kunden finns inte!"));

        Room room = roomRepository.findById(
                        bookingDto.getRoomId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Rummet finns inte!"));

        if (!bookingDto.getCheckOutDate()
                .isAfter(bookingDto.getCheckInDate())) {

            throw new IllegalArgumentException(
                    "Utcheckning måste ske efter incheckning!");
        }

        if (bookingDto.getNumberOfGuests() < 1) {
            throw new IllegalArgumentException(
                    "Måste vara minst 1 gäst!");
        }

        if (room.getExtraBeds() < 0) {
            throw new IllegalArgumentException(
                    "Antal extrasängar kan inte vara negativt");
        }

        if (room.getType() == RoomType.SINGLE
                && room.getExtraBeds() > 0) {

            throw new IllegalArgumentException(
                    "Enkelrum kan ej ha extrasängar!");
        }

        if (room.getType() == RoomType.DOUBLE
                && room.getSize() == RoomSize.SMALL
                && room.getExtraBeds() > 1) {

            throw new IllegalArgumentException(
                    "Litet dubbelrum kan max ha en extrasäng!");
        }

        if (room.getType() == RoomType.DOUBLE
                && room.getSize() == RoomSize.LARGE
                && room.getExtraBeds() > 2) {

            throw new IllegalArgumentException(
                    "Stort dubbelrum kan max ha två extrasängar!");
        }

        if (bookingDto.getNumberOfGuests() > getMaxGuests(room)) {
            throw new IllegalArgumentException(
                    "OBS! För många gäster!");
        }

        boolean roomBooked = bookingRepository.roomIsBooked(
                bookingDto.getRoomId(),
                bookingDto.getCheckInDate(),
                bookingDto.getCheckOutDate(),
                bookingId
        );

        if (roomBooked) {
            throw new IllegalArgumentException(
                    "Rummet är redan uppbokat dessa datum");
        }

        booking.setCustomer(customer);
        booking.setRoom(room);
        booking.setCheckInDate(bookingDto.getCheckInDate());
        booking.setCheckOutDate(bookingDto.getCheckOutDate());
        booking.setNumberOfGuests(bookingDto.getNumberOfGuests());

        Booking savedBooking = bookingRepository.save(booking);

        return toBookingDto(savedBooking);
    }

    private int getMaxGuests(Room room) {

        if (room.getType() == RoomType.SINGLE) {
            return 1 + room.getExtraBeds();
        }

        return 2 + room.getExtraBeds();
    }

    private BookingDto toBookingDto(Booking booking) {

        BookingDto dto = new BookingDto();

        dto.setId(booking.getId());
        dto.setCustomerId(booking.getCustomer().getId());
        dto.setRoomId(booking.getRoom().getId());
        dto.setCheckInDate(booking.getCheckInDate());
        dto.setCheckOutDate(booking.getCheckOutDate());
        dto.setNumberOfGuests(booking.getNumberOfGuests());

        return dto;
    }

    private RoomDetailedDto toRoomDto(Room room) {

        RoomDetailedDto dto = new RoomDetailedDto();

        dto.setId(room.getId());
        dto.setType(room.getType());
        dto.setSize(room.getSize());
        dto.setExtraBeds(room.getExtraBeds());

        return dto;
    }
}