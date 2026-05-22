package org.example.pensionatkademina.service;

import lombok.RequiredArgsConstructor;
import org.example.pensionatkademina.dto.BookingDto;
import org.example.pensionatkademina.model.Booking;
import org.example.pensionatkademina.model.Customer;
import org.example.pensionatkademina.model.Room;
import org.example.pensionatkademina.repository.BookingRepository;
import org.example.pensionatkademina.repository.CustomerRepository;
import org.example.pensionatkademina.repository.RoomRepository;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;
    private final RoomRepository roomRepository;


    public BookingDto createBooking(BookingDto bookingDto){

        Customer customer = customerRepository
                .findById(bookingDto.getCustomerId())
                .orElseThrow();


        Room room = roomRepository
                .findById(bookingDto.getRoomId())
                .orElseThrow();

        if (!bookingDto.getCheckOutDate().isAfter(bookingDto.getCheckInDate())){
            throw new IllegalArgumentException("Check-out date must be after check-in date");
        }

        Booking booking = new Booking();

        booking.setCustomer(customer);
        booking.setRoom(room);
        booking.setCheckInDate(bookingDto.getCheckInDate());
        booking.setCheckOutDate(bookingDto.getCheckOutDate());
        booking.setNumberOfGuests(bookingDto.getNumberOfGuests());

        Booking savedBooking = bookingRepository.save(booking);
        return toDto(savedBooking);


    }

    private BookingDto toDto(Booking booking){
        BookingDto dto = new BookingDto();

        dto.setId(booking.getId());
        dto.setCustomerId(booking.getCustomer().getId());
        dto.setRoomId(booking.getRoom().getId());
        dto.setCheckInDate(booking.getCheckInDate());
        dto.setCheckOutDate(booking.getCheckOutDate());
        dto.setNumberOfGuests(booking.getNumberOfGuests());

        return dto;
    }




}
