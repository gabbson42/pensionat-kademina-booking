package org.example.pensionatkademina.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class BookingDto {

    private Long id;

    @NotNull(message = "Kund måste väljas!")
    private Long customerId;

    @NotNull(message = "Rum måste väljas!")
    private Long roomId;

    @NotNull(message = "Incheckningsdatum måste anges!")
    @FutureOrPresent(message = "Incheckningsdatum kan inte vara i dåtid!")
    private LocalDate checkInDate;

    @NotNull(message = "Utcheckningsdatum måste anges!")
    private LocalDate checkOutDate;

    @Min(value = 1, message = "Minst 1 gäst krävs!")
    private int numberOfGuests;

}
















