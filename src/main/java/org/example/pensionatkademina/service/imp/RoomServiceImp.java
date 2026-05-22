package org.example.pensionatkademina.service.imp;

import lombok.RequiredArgsConstructor;
import org.example.pensionatkademina.dto.RoomDetailedDto;
import org.example.pensionatkademina.dto.RoomReservationDto;
import org.example.pensionatkademina.model.Booking;
import org.example.pensionatkademina.model.Room;
import org.example.pensionatkademina.repository.RoomRepository;
import org.example.pensionatkademina.service.RoomService;
import org.example.pensionatkademina.utility.RoomSize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomServiceImp implements RoomService {

    private final RoomRepository repo;

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
    public RoomDetailedDto roomToRoomDto(Room room) {

        List<RoomReservationDto> roomBookings = bookingsToReservations(room.getBooking());

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
        return Room.builder()
                .id(dto.getId())
                .type(dto.getType())
                .size(dto.getSize())
                .extraBeds(dto.getExtraBeds())
                .booking(new ArrayList<>())
                .build();
    }

    @Override
    public List<RoomDetailedDto> getAllRoom(){
        return repo.findAll().stream().map(room -> roomToRoomDto(room)).toList();
    }

    @Override
    public void addRoom(RoomDetailedDto dto){
        Room room = roomDtoToRoom(dto);
        repo.save(room);
    }

    @Override
    public void setExtraBeds(Long roomId, int amount) {
        Room room = repo.getRoomsById(roomId);
        if(room.getSize() == RoomSize.SMALL && amount <= 1
        || room.getSize() == RoomSize.LARGE && amount <= 2) {
            room.setExtraBeds(amount);
            repo.save(room);
        }
    }

}
