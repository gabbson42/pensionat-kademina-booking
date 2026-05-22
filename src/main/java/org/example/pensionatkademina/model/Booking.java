package org.example.pensionatkademina.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
