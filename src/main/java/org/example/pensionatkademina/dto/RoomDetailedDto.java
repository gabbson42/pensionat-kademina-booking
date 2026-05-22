package org.example.pensionatkademina.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomDetailedDto {

    private Long id;
    private String type;
    private int beds;
    private List<RoomReservationDto> roomReservations;

}
