package org.example.pensionatkademina.controller;

import lombok.RequiredArgsConstructor;
import org.example.pensionatkademina.dto.RoomDetailedDto;
import org.example.pensionatkademina.service.imp.RoomServiceImp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RoomController {

    private final RoomServiceImp roomService;

    @GetMapping("/Rooms")
    public List<RoomDetailedDto> Rooms(){
        return roomService.getAllRoom();
    }

    @PutMapping("/AddRoom")
    public List<RoomDetailedDto> AddRoom(@RequestBody RoomDetailedDto roomDto){
        roomService.addRoom(roomDto);
        return roomService.getAllRoom();
    }
}