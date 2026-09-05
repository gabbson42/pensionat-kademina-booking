package org.example.pensionatkademina.dto;

public class BookingCheckResponseDto {

    private boolean booked;

    public BookingCheckResponseDto(boolean booked) {
        this.booked = booked;

    }
    public boolean isBooked() {
        return booked;
    }
    public void setBooked(boolean booked) {
        this.booked = booked;
    }
}
