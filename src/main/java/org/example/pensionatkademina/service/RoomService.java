package org.example.pensionatkademina.service;


import org.example.pensionatkademina.dto.RoomDto;
import org.example.pensionatkademina.model.Room;

public interface RoomService {

    RoomDto roomToRoomDto(Room room);

    Room roomDtoToRoom(RoomDto roomDTO);
}