package org.example.pensionatkademina.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue
    private Long id;

    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int numberOfGuests;
    private int extraBeds;

    private Long customerId;

    @ManyToOne
    private Room room;
}