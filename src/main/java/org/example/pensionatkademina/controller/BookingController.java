package org.example.pensionatkademina.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.pensionatkademina.dto.BookingDto;
import org.example.pensionatkademina.service.BookingService;
import org.example.pensionatkademina.service.imp.CustomerServiceImp;
import org.example.pensionatkademina.service.imp.RoomServiceImp;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
@RequestMapping("booking")

public class BookingController {

    private final BookingService bookingService;
    private final CustomerServiceImp customerService;
    private final RoomServiceImp roomService;

    @RequestMapping
    public String allBookings(Model model) {
        model.addAttribute("bookings", bookingService.getAllBookings());
        model.addAttribute("bookingDto", new BookingDto());
        model.addAttribute("customers", customerService.getAllCustomers());
        model.addAttribute("rooms", roomService.getAllRoom());

        return "booking";
    }

    @PostMapping("add")
    public String addBooking(@Valid @ModelAttribute BookingDto bookingDto,
                             RedirectAttributes redirectAttributes) {

        try {
            bookingService.createBooking(bookingDto);
            redirectAttributes.addFlashAttribute("message", "Booking created!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/booking";
    }

    @GetMapping("edit/{id}")
    public String editBooking(@PathVariable Long id, Model model) {
        BookingDto bookingDto = bookingService.getBookingById(id);

        model.addAttribute("bookingDto", bookingDto);
        model.addAttribute("bookings", bookingService.getAllBookings());
        model.addAttribute("customers", customerService.getAllCustomers());
        model.addAttribute("rooms", roomService.getAllRoom());

        return "booking";
    }

    @PostMapping("update/{id}")
    public String updateBooking(@PathVariable Long id,
                                @Valid @ModelAttribute BookingDto bookingDto,
                                RedirectAttributes redirectAttributes) {

        try {
            bookingService.updateBooking(id, bookingDto);
            redirectAttributes.addFlashAttribute("message", "Booking updated!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/booking";
    }

    @PostMapping("delete/{id}")
    public String deleteBooking(@PathVariable Long id,
                                RedirectAttributes redirectAttributes) {

        try {
            bookingService.deleteBooking(id);
            redirectAttributes.addFlashAttribute("message", "Booking deleted!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/booking";
    }

    @GetMapping("available")
    public String searchAvailableRooms(@RequestParam LocalDate checkInDate,
                                       @RequestParam LocalDate checkOutDate,
                                       @RequestParam int numberOfGuests,
                                       Model model) {

        model.addAttribute("availableRooms", bookingService.searchAvailableRooms(
                checkInDate,
                checkOutDate,
                numberOfGuests
        ));

        return "booking";
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public String handleValidationException(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute(
                "errorMessage",
                "Verify that customer, room, dates " +
                        "and amount of visitors is properly filled out thank you."
        );

        return "redirect:/booking";
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleArgumentException(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage",
                "Check in date can't be in the past.");
        return "redirect:/booking";
    }
}