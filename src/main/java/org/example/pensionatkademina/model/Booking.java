package org.example.pensionatkademina.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
    private Customer customer;

    // TODO: Add room relation when Room entity is ready
    // @ManyToOne
    // private Room room;




}
