package org.example.pensionatkademina.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue
    private Long id;

    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int numberOfGuests;

    @ManyToOne
    @JoinColumn
    private Customer customer;


    @ManyToOne
    private Room room;




}
