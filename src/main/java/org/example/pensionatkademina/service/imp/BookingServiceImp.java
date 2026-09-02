package org.example.pensionatkademina.service.imp;

import lombok.RequiredArgsConstructor;
import org.example.pensionatkademina.dto.BookingDto;
import org.example.pensionatkademina.dto.RoomDetailedDto;
import org.example.pensionatkademina.model.Booking;
import org.example.pensionatkademina.model.Room;
import org.example.pensionatkademina.repository.BookingRepository;
import org.example.pensionatkademina.repository.RoomRepository;
import org.example.pensionatkademina.service.BookingService;
import org.example.pensionatkademina.utility.RoomSize;
import org.example.pensionatkademina.utility.RoomType;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImp implements BookingService {

    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;
    private final RoomRepository roomRepository;

    @Override
    public List<BookingDto> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(this::toBookingDto)
                .toList();
    }

    @Override
    public BookingDto getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found!"));

        return toBookingDto(booking);
    }

    @Override
    public BookingDto createBooking(BookingDto bookingDto) {
        Booking booking = new Booking();

        return saveBooking(booking, bookingDto, null);
    }

    @Override
    public BookingDto updateBooking(Long id, BookingDto bookingDto) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found!"));

        return saveBooking(booking, bookingDto, id);
    }

    @Override
    public void deleteBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new IllegalArgumentException("Booking does not exist!");
        }

        bookingRepository.deleteById(id);
    }

    @Override
    public List<RoomDetailedDto> searchAvailableRooms(LocalDate checkInDate,
                                                      LocalDate checkOutDate,
                                                      int numberOfGuests) {

        if (!checkOutDate.isAfter(checkInDate)) {
            throw new IllegalArgumentException("Check out must occur after check in!");
        }

        if (numberOfGuests < 1) {
            throw new IllegalArgumentException("Minimum amount of guests is 1!");
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

        Customer customer = customerRepository.findById(bookingDto.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer does not exist!"));

        Room room = roomRepository.findById(bookingDto.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("Room does not exist!"));

        if (!bookingDto.getCheckOutDate().isAfter(bookingDto.getCheckInDate())) {
            throw new IllegalArgumentException("Check out must occur after check in!");
        }

        if (bookingDto.getNumberOfGuests() < 1) {
            throw new IllegalArgumentException("Minimum amount of guests is 1!");
        }

        if (room.getType() == RoomType.SINGLE && bookingDto.getExtraBeds() > 0) {
            throw new IllegalArgumentException("Single rooms can't have extra beds!");
        }

        if (room.getType() == RoomType.DOUBLE
                && room.getSize() == RoomSize.SMALL
                && bookingDto.getExtraBeds() > 1) {

            throw new IllegalArgumentException("Small double rooms can have a maximum of 1 extra beds!");
        }

        if (room.getType() == RoomType.DOUBLE
                && room.getSize() == RoomSize.LARGE
                && bookingDto.getExtraBeds() > 2) {

            throw new IllegalArgumentException("Large double rooms can have a maximum of 2 extra beds!");
        }

        if (bookingDto.getNumberOfGuests() > getMaxGuests(room)) {
            throw new IllegalArgumentException("Too many guests!");
        }

        boolean roomBooked = bookingRepository.roomIsBooked(
                bookingDto.getRoomId(),
                bookingDto.getCheckInDate(),
                bookingDto.getCheckOutDate(),
                bookingId
        );

        if (roomBooked) {
            throw new IllegalArgumentException("The room is already booked on the selected dates.");
        }

        booking.setCustomer(customer);
        booking.setRoom(room);
        booking.setCheckInDate(bookingDto.getCheckInDate());
        booking.setCheckOutDate(bookingDto.getCheckOutDate());
        booking.setNumberOfGuests(bookingDto.getNumberOfGuests());
        booking.setExtraBeds(bookingDto.getExtraBeds());

        Booking savedBooking = bookingRepository.save(booking);

        return toBookingDto(savedBooking);
    }

    private int getMaxGuests(Room room) {
        if (room.getType() == RoomType.SINGLE) {
            return 1;
        }
        if (room.getSize() == RoomSize.SMALL) {
            return 3;
        }else
            return 4;
    }

    private BookingDto toBookingDto(Booking booking) {
        BookingDto dto = new BookingDto();

        dto.setId(booking.getId());
        dto.setCustomerId(booking.getCustomer().getId());
        dto.setRoomId(booking.getRoom().getId());
        dto.setCheckInDate(booking.getCheckInDate());
        dto.setCheckOutDate(booking.getCheckOutDate());
        dto.setNumberOfGuests(booking.getNumberOfGuests());
        dto.setExtraBeds(booking.getExtraBeds());

        return dto;
    }

    private RoomDetailedDto toRoomDto(Room room) {
        RoomDetailedDto dto = new RoomDetailedDto();

        dto.setId(room.getId());
        dto.setType(room.getType());
        dto.setSize(room.getSize());

        return dto;
    }
}