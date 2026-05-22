package org.example.pensionatkademina.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.pensionatkademina.utility.RoomSize;
import org.example.pensionatkademina.utility.RoomType;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomDetailedDto {

    private Long id;
    private RoomType type;
    private RoomSize size;
    private int extraBeds;
    private List<RoomReservationDto> roomReservations;

}
