package org.example.pensionatkademina.service;


import org.example.pensionatkademina.dto.RoomDetailedDto;
import org.example.pensionatkademina.dto.RoomReservationDto;
import org.example.pensionatkademina.model.Booking;
import org.example.pensionatkademina.model.Room;
import java.util.List;

public interface RoomService {

    List<RoomReservationDto> bookingsToReservations(List<Booking> bookings);

    List<Booking> reservationsToBookings(List<RoomReservationDto> reservations);

    List<RoomDetailedDto> getAllRoom();

    Room findById(int id);

    void addRoom(RoomDetailedDto dto);

    void setExtraBeds(Long roomId, int amount);

    RoomDetailedDto roomToRoomDto(Room room);

    Room roomDtoToRoom(RoomDetailedDto roomDTO);

    void deleteAll();

    boolean existsById(Long id);

}