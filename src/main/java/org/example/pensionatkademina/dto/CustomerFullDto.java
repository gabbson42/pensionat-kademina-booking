package org.example.pensionatkademina.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.pensionatkademina.model.Booking;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerFullDto {

    private Long id;
    private String name;
    private List<Booking> bookings;
}
