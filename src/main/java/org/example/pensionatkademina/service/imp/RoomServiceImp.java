package org.example.pensionatkademina.service.imp;

import lombok.RequiredArgsConstructor;
import org.example.pensionatkademina.dto.RoomDetailedDto;
import org.example.pensionatkademina.dto.RoomReservationDto;
import org.example.pensionatkademina.model.Booking;
import org.example.pensionatkademina.model.Customer;
import org.example.pensionatkademina.model.Room;
import org.example.pensionatkademina.repository.BookingRepository;
import org.example.pensionatkademina.repository.CustomerRepository;
import org.example.pensionatkademina.repository.RoomRepository;
import org.example.pensionatkademina.service.RoomService;
import org.example.pensionatkademina.utility.RoomSize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomServiceImp implements RoomService {

    private final RoomRepository roomRepo;
    private final BookingRepository bookingRepo;

    @Override
    public List<RoomReservationDto> bookingsToReservations(List<Booking> bookings) {

        List<RoomReservationDto> listOfReservations = new ArrayList<>();

        for (Booking booking : bookings) {
            listOfReservations.add(
                RoomReservationDto.builder()
                    .id(booking.getId())
                    .customerName(booking.getCustomer().getName())
                    .checkInDate(booking.getCheckInDate())
                    .checkOutDate(booking.getCheckOutDate())
                    .numberOfGuests(booking.getNumberOfGuests())
                    .build()
            );
        }

        return listOfReservations;
    }

    @Override
    public List<Booking> reservationsToBookings(List<RoomReservationDto> reservations) {

        List<Booking> listOfBookings = new ArrayList<>();

        for (RoomReservationDto dto : reservations) {

            Long id = dto.getId();
            Booking booking = bookingRepo.findBookingById(id);

            booking.setCheckInDate(dto.getCheckInDate());
            booking.setCheckOutDate(dto.getCheckOutDate());
            booking.setNumberOfGuests(dto.getNumberOfGuests());
            booking.setRoom(roomRepo.getRoomById(id));

            listOfBookings.add(booking);

        }

        return listOfBookings;
    }


    @Override
    public RoomDetailedDto roomToRoomDto(Room room) {

        List<RoomReservationDto> roomBookings;

        if(room.getBooking() != null) {
            roomBookings = bookingsToReservations(room.getBooking());
        }else roomBookings = new ArrayList<>();

        return RoomDetailedDto.builder()
                .id(room.getId())
                .type(room.getType())
                .size(room.getSize())
                .extraBeds(room.getExtraBeds())
                .roomReservations(roomBookings)
                .build();
    }

    @Override
    public Room roomDtoToRoom(RoomDetailedDto dto) {

        List<Booking> roomBookings;
        if(dto.getRoomReservations() != null) {
            roomBookings = reservationsToBookings(dto.getRoomReservations());
        }else  roomBookings = new ArrayList<>();

        return Room.builder()
                .id(dto.getId())
                .type(dto.getType())
                .size(dto.getSize())
                .extraBeds(dto.getExtraBeds())
                .booking(roomBookings)
                .build();
    }

    @Override
    public List<RoomDetailedDto> getAllRoom(){
        return roomRepo.findAll().stream().map(room -> roomToRoomDto(room)).toList();
    }

    @Override
    public void addRoom(RoomDetailedDto dto){
        Room room = roomDtoToRoom(dto);
        roomRepo.save(room);
    }

    @Override
    public void setExtraBeds(Long roomId, int amount) {
        Room room = roomRepo.getRoomById(roomId);
        if(room.getSize() == RoomSize.SMALL && amount <= 1
        || room.getSize() == RoomSize.LARGE && amount <= 2) {
            room.setExtraBeds(amount);
            roomRepo.save(room);
        }
    }

    @Override
    public Room findById(int input) {
        Long id = (long) input;
        return roomRepo.getRoomById(id);
    }

    @Override
    public void deleteAll() {
        roomRepo.deleteAll();
        bookingRepo.deleteAll();
    }

    @Override
    public boolean existsById(Long id) {
        return roomRepo.existsById(id);
    }

}
