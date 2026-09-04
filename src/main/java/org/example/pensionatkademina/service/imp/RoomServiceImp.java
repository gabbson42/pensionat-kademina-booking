package org.example.pensionatkademina.service.imp;

import lombok.RequiredArgsConstructor;
import org.example.pensionatkademina.client.CustomerClient;
import org.example.pensionatkademina.dto.RoomDetailedDto;
import org.example.pensionatkademina.dto.RoomReservationDto;
import org.example.pensionatkademina.model.Booking;
import org.example.pensionatkademina.model.Room;
import org.example.pensionatkademina.repository.RoomRepository;
import org.example.pensionatkademina.service.RoomService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomServiceImp implements RoomService {

    private final RoomRepository roomRepo;
    private final CustomerClient customerClient;

    @Override
    public List<RoomReservationDto> bookingsToReservations(List<Booking> bookings) {

        List<RoomReservationDto> listOfReservations = new ArrayList<>();

        for (Booking booking : bookings) {
            String customerName;
            try {
                customerName = customerClient.findCustomerById(booking.getCustomerId()).getName();
            } catch (NullPointerException e) {
                customerName = "Data unavailable";
            }

            listOfReservations.add(
                    RoomReservationDto.builder()
                            .id(booking.getId())
                            .customerName(customerName)
                            .checkInDate(booking.getCheckInDate())
                            .checkOutDate(booking.getCheckOutDate())
                            .numberOfGuests(booking.getNumberOfGuests())
                            .extraBeds(booking.getExtraBeds())
                            .build()
            );
        }

        return listOfReservations;
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
                .roomReservations(roomBookings)
                .build();
    }


    @Override
    @Transactional(readOnly = true)
    public List<RoomDetailedDto> getAllRoom(){
        return roomRepo.findAll().stream().map(room -> roomToRoomDto(room)).toList();
    }

    @Override
    public boolean existsById(Long id) {
        return roomRepo.existsById(id);
    }

}
