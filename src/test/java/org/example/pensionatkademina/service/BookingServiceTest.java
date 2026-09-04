package org.example.pensionatkademina.service;

import org.example.pensionatkademina.dto.BookingDto;
import org.example.pensionatkademina.dto.RoomDetailedDto;
import org.example.pensionatkademina.model.Booking;
import org.example.pensionatkademina.model.Room;
import org.example.pensionatkademina.repository.BookingRepository;
import org.example.pensionatkademina.repository.RoomRepository;
import org.example.pensionatkademina.service.imp.BookingServiceImp;
import org.example.pensionatkademina.utility.RoomSize;
import org.example.pensionatkademina.utility.RoomType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private BookingServiceImp bookingService;

    private Customer customer;
    private Room doubleSmallRoom;
    private Room doubleLargeRoom;
    private Room singleRoom;
    private Booking booking1;
    private Booking booking2;
    private BookingDto validBookingDto;

    @BeforeEach
    void setUp() {
        customer = Customer.builder()
                .id(1L)
                .name("Raul")
                .build();

        doubleSmallRoom = Room.builder()
                .id(2L)
                .type(RoomType.DOUBLE)
                .size(RoomSize.SMALL)
                .build();

        doubleLargeRoom = Room.builder()
                .id(3L)
                .type(RoomType.DOUBLE)
                .size(RoomSize.LARGE)
                .build();

        singleRoom = Room.builder()
                .id(4L)
                .type(RoomType.SINGLE)
                .size(RoomSize.SMALL)
                .build();

        booking1 = Booking.builder()
                .id(10L)
                .customer(customer)
                .room(doubleSmallRoom)
                .checkInDate(LocalDate.of(2026, 5, 27))
                .checkOutDate(LocalDate.of(2026, 5, 29))
                .numberOfGuests(2)
                .extraBeds(0)
                .build();

        booking2 = Booking.builder()
                .id(11L)
                .customer(customer)
                .room(doubleLargeRoom)
                .checkInDate(LocalDate.of(2026, 6, 1))
                .checkOutDate(LocalDate.of(2026, 6, 3))
                .numberOfGuests(3)
                .extraBeds(1)
                .build();

        validBookingDto = BookingDto.builder()
                .customerId(1L)
                .roomId(2L)
                .checkInDate(LocalDate.of(2026, 7, 1))
                .checkOutDate(LocalDate.of(2026, 7, 3))
                .numberOfGuests(2)
                .extraBeds(0)
                .build();
    }

    @Test
    void getBookingById_shouldReturnBookingDto_whenBookingExists() {
        when(bookingRepository.findById(10L)).thenReturn(Optional.of(booking1));

        BookingDto result = bookingService.getBookingById(10L);

        assertEquals(10L, result.getId());
        assertEquals(1L, result.getCustomerId());
        assertEquals(2L, result.getRoomId());
        assertEquals(LocalDate.of(2026, 5, 27), result.getCheckInDate());
        assertEquals(LocalDate.of(2026, 5, 29), result.getCheckOutDate());
        assertEquals(2, result.getNumberOfGuests());
        assertEquals(0, result.getExtraBeds());

        verify(bookingRepository).findById(10L);
    }

    @Test
    void getBookingById_shouldThrowException_whenBookingDoesNotExist() {
        when(bookingRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                bookingService.getBookingById(99L)
        );

        verify(bookingRepository).findById(99L);
    }

    @Test
    void getAllBookings_shouldReturnBookingDtoList_whenBookingsExist() {
        when(bookingRepository.findAll()).thenReturn(List.of(booking1, booking2));

        List<BookingDto> result = bookingService.getAllBookings();

        assertEquals(2, result.size());

        assertEquals(10L, result.get(0).getId());
        assertEquals(11L, result.get(1).getId());

        assertEquals(1L, result.get(0).getCustomerId());
        assertEquals(2L, result.get(0).getRoomId());

        assertEquals(1L, result.get(1).getCustomerId());
        assertEquals(3L, result.get(1).getRoomId());

        verify(bookingRepository).findAll();
    }

    @Test
    void createBooking_shouldSaveBooking_whenDataIsValid() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(roomRepository.findById(2L)).thenReturn(Optional.of(doubleSmallRoom));
        when(bookingRepository.roomIsBooked(
                2L,
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 3),
                null
        )).thenReturn(false);

        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> {
            Booking booking = invocation.getArgument(0);
            booking.setId(20L);
            return booking;
        });

        BookingDto result = bookingService.createBooking(validBookingDto);

        assertEquals(20L, result.getId());
        assertEquals(1L, result.getCustomerId());
        assertEquals(2L, result.getRoomId());
        assertEquals(LocalDate.of(2026, 7, 1), result.getCheckInDate());
        assertEquals(LocalDate.of(2026, 7, 3), result.getCheckOutDate());
        assertEquals(2, result.getNumberOfGuests());
        assertEquals(0, result.getExtraBeds());

        verify(customerRepository).findById(1L);
        verify(roomRepository).findById(2L);
        verify(bookingRepository).roomIsBooked(
                2L,
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 3),
                null
        );
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void createBooking_shouldThrowException_whenCheckOutIsNotAfterCheckIn() {
        BookingDto dto = BookingDto.builder()
                .customerId(1L)
                .roomId(2L)
                .checkInDate(LocalDate.of(2026, 7, 3))
                .checkOutDate(LocalDate.of(2026, 7, 3))
                .numberOfGuests(2)
                .extraBeds(0)
                .build();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(roomRepository.findById(2L)).thenReturn(Optional.of(doubleSmallRoom));

        assertThrows(IllegalArgumentException.class, () ->
                bookingService.createBooking(dto)
        );

        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void createBooking_shouldThrowException_whenCustomerDoesNotExist() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                bookingService.createBooking(validBookingDto)
        );

        verify(customerRepository).findById(1L);
        verify(roomRepository, never()).findById(anyLong());
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void createBooking_shouldThrowException_whenRoomDoesNotExist() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(roomRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                bookingService.createBooking(validBookingDto)
        );

        verify(customerRepository).findById(1L);
        verify(roomRepository).findById(2L);
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void createBooking_shouldThrowException_whenNumberOfGuestsIsLessThanOne() {
        BookingDto dto = BookingDto.builder()
                .customerId(1L)
                .roomId(2L)
                .checkInDate(LocalDate.of(2026, 7, 1))
                .checkOutDate(LocalDate.of(2026, 7, 3))
                .numberOfGuests(0)
                .extraBeds(0)
                .build();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(roomRepository.findById(2L)).thenReturn(Optional.of(doubleSmallRoom));

        assertThrows(IllegalArgumentException.class, () ->
                bookingService.createBooking(dto)
        );

        verify(bookingRepository, never()).save(any(Booking.class));
    }


    @Test
    void createBooking_shouldThrowException_whenSingleRoomHasExtraBeds() {
        BookingDto dto = BookingDto.builder()
                .customerId(1L)
                .roomId(4L)
                .checkInDate(LocalDate.of(2026, 7, 1))
                .checkOutDate(LocalDate.of(2026, 7, 3))
                .numberOfGuests(1)
                .extraBeds(1)
                .build();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(roomRepository.findById(4L)).thenReturn(Optional.of(singleRoom));

        assertThrows(IllegalArgumentException.class, () ->
                bookingService.createBooking(dto)
        );

        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void createBooking_shouldThrowException_whenSmallDoubleRoomHasTooManyExtraBeds() {
        BookingDto dto = BookingDto.builder()
                .customerId(1L)
                .roomId(2L)
                .checkInDate(LocalDate.of(2026, 7, 1))
                .checkOutDate(LocalDate.of(2026, 7, 3))
                .numberOfGuests(2)
                .extraBeds(2)
                .build();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(roomRepository.findById(2L)).thenReturn(Optional.of(doubleSmallRoom));

        assertThrows(IllegalArgumentException.class, () ->
                bookingService.createBooking(dto)
        );

        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void createBooking_shouldThrowException_whenLargeDoubleRoomHasTooManyExtraBeds() {
        BookingDto dto = BookingDto.builder()
                .customerId(1L)
                .roomId(3L)
                .checkInDate(LocalDate.of(2026, 7, 1))
                .checkOutDate(LocalDate.of(2026, 7, 3))
                .numberOfGuests(2)
                .extraBeds(3)
                .build();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(roomRepository.findById(3L)).thenReturn(Optional.of(doubleLargeRoom));

        assertThrows(IllegalArgumentException.class, () ->
                bookingService.createBooking(dto)
        );

        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void createBooking_shouldThrowException_whenTooManyGuestsForRoom() {
        BookingDto dto = BookingDto.builder()
                .customerId(1L)
                .roomId(2L)
                .checkInDate(LocalDate.of(2026, 7, 1))
                .checkOutDate(LocalDate.of(2026, 7, 3))
                .numberOfGuests(4)
                .extraBeds(0)
                .build();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(roomRepository.findById(2L)).thenReturn(Optional.of(doubleSmallRoom));

        assertThrows(IllegalArgumentException.class, () ->
                bookingService.createBooking(dto)
        );

        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void createBooking_shouldThrowException_whenRoomIsAlreadyBooked() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(roomRepository.findById(2L)).thenReturn(Optional.of(doubleSmallRoom));
        when(bookingRepository.roomIsBooked(
                2L,
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 3),
                null
        )).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () ->
                bookingService.createBooking(validBookingDto)
        );

        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void updateBooking_shouldUpdateBooking_whenDataIsValid() {
        BookingDto updateDto = BookingDto.builder()
                .customerId(1L)
                .roomId(3L)
                .checkInDate(LocalDate.of(2026, 8, 1))
                .checkOutDate(LocalDate.of(2026, 8, 4))
                .numberOfGuests(3)
                .extraBeds(1)
                .build();

        when(bookingRepository.findById(10L)).thenReturn(Optional.of(booking1));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(roomRepository.findById(3L)).thenReturn(Optional.of(doubleLargeRoom));
        when(bookingRepository.roomIsBooked(
                3L,
                LocalDate.of(2026, 8, 1),
                LocalDate.of(2026, 8, 4),
                10L
        )).thenReturn(false);

        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BookingDto result = bookingService.updateBooking(10L, updateDto);

        assertEquals(10L, result.getId());
        assertEquals(1L, result.getCustomerId());
        assertEquals(3L, result.getRoomId());
        assertEquals(LocalDate.of(2026, 8, 1), result.getCheckInDate());
        assertEquals(LocalDate.of(2026, 8, 4), result.getCheckOutDate());
        assertEquals(3, result.getNumberOfGuests());
        assertEquals(1, result.getExtraBeds());

        verify(bookingRepository).findById(10L);
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void updateBooking_shouldThrowException_whenBookingDoesNotExist() {
        when(bookingRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                bookingService.updateBooking(99L, validBookingDto)
        );

        verify(bookingRepository).findById(99L);
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void deleteBooking_shouldDeleteBooking_whenBookingExists() {
        when(bookingRepository.existsById(10L)).thenReturn(true);

        bookingService.deleteBooking(10L);

        verify(bookingRepository).existsById(10L);
        verify(bookingRepository).deleteById(10L);
    }

    @Test
    void deleteBooking_shouldThrowException_whenBookingDoesNotExist() {
        when(bookingRepository.existsById(99L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () ->
                bookingService.deleteBooking(99L)
        );

        verify(bookingRepository).existsById(99L);
        verify(bookingRepository, never()).deleteById(anyLong());
    }

    @Test
    void searchAvailableRooms_shouldReturnOnlyAvailableRoomsThatFitGuests() {
        Room tooSmallSingleRoom = Room.builder()
                .id(5L)
                .type(RoomType.SINGLE)
                .size(RoomSize.SMALL)
                .build();

        when(roomRepository.findAll()).thenReturn(List.of(doubleSmallRoom, doubleLargeRoom, tooSmallSingleRoom));

        when(bookingRepository.roomIsBooked(
                2L,
                LocalDate.of(2026, 9, 1),
                LocalDate.of(2026, 9, 3),
                null
        )).thenReturn(false);

        when(bookingRepository.roomIsBooked(
                3L,
                LocalDate.of(2026, 9, 1),
                LocalDate.of(2026, 9, 3),
                null
        )).thenReturn(true);

        List<RoomDetailedDto> result = bookingService.searchAvailableRooms(
                LocalDate.of(2026, 9, 1),
                LocalDate.of(2026, 9, 3),
                2
        );

        assertEquals(1, result.size());
        assertEquals(2L, result.get(0).getId());
        assertEquals(RoomType.DOUBLE, result.get(0).getType());
        assertEquals(RoomSize.SMALL, result.get(0).getSize());

        verify(roomRepository).findAll();
    }

    @Test
    void searchAvailableRooms_shouldThrowException_whenCheckOutIsNotAfterCheckIn() {
        assertThrows(IllegalArgumentException.class, () ->
                bookingService.searchAvailableRooms(
                        LocalDate.of(2026, 9, 3),
                        LocalDate.of(2026, 9, 3),
                        2
                )
        );

        verify(roomRepository, never()).findAll();
    }

    @Test
    void searchAvailableRooms_shouldThrowException_whenNumberOfGuestsIsLessThanOne() {
        assertThrows(IllegalArgumentException.class, () ->
                bookingService.searchAvailableRooms(
                        LocalDate.of(2026, 9, 1),
                        LocalDate.of(2026, 9, 3),
                        0
                )
        );

        verify(roomRepository, never()).findAll();
    }
}