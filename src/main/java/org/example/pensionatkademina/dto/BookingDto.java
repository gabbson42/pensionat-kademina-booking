package org.example.pensionatkademina.dto;


import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {

    private Long id;

    // @NotNull(message = " ")
    private Long customerId;

    private Long roomId;

    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    private int numberOfGuests;



















}
