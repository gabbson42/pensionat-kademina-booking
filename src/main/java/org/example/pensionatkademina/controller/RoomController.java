package org.example.pensionatkademina.controller;

import lombok.RequiredArgsConstructor;
import org.example.pensionatkademina.dto.RoomDetailedDto;
import org.example.pensionatkademina.service.imp.RoomServiceImp;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RoomController {

    private final RoomServiceImp roomService;

    @GetMapping("/Rooms")
    public List<RoomDetailedDto> Rooms(){
        return roomService.getAllRoom();
    }

    @PostMapping("/AddRoom")
    public List<RoomDetailedDto> AddRoom(@RequestBody RoomDetailedDto roomDto){
        roomService.addRoom(roomDto);
        return roomService.getAllRoom();
    }

    @PutMapping("/extraBeds")
    public List<RoomDetailedDto> extraBeds(Long roomId, Integer amount){
        roomService.setExtraBeds(roomId, amount);
        return roomService.getAllRoom();
    }
}