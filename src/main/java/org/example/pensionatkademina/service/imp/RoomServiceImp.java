package org.example.pensionatkademina.service.imp;

import lombok.RequiredArgsConstructor;
import org.example.pensionatkademina.dto.RoomDto;
import org.example.pensionatkademina.model.Room;
import org.example.pensionatkademina.repository.RoomRepository;
import org.example.pensionatkademina.service.RoomService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomServiceImp implements RoomService {

    private final RoomRepository repo;

    @Override
    public RoomDto roomToRoomDto(Room room) {
        return RoomDto.builder().id(room.getId()).type(room.getType()).beds(room.getBeds()).build();
    }

    @Override
    public Room roomDtoToRoom(RoomDto dto) {
        return Room.builder().id(dto.getId()).type(dto.getType()).beds(dto.getBeds()).build();
    }

    public List<RoomDto> getAllRoom(){
        return repo.findAll().stream().map(room -> roomToRoomDto(room)).toList();
    }

    public void addRoom(RoomDto dto){
        Room room = roomDtoToRoom(dto);
        repo.save(room);
    }

}
