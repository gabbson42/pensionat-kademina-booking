package org.example.pensionatkademina.service.imp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.pensionatkademina.client.CustomerClient;
import org.example.pensionatkademina.dto.BookingDto;
import org.example.pensionatkademina.dto.CustomerDto;
import org.example.pensionatkademina.dto.CustomerFullDto;
import org.example.pensionatkademina.model.Booking;
import org.example.pensionatkademina.repository.BookingRepository;
import org.example.pensionatkademina.service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImp implements CustomerService {

    private final CustomerClient customerClient;
    private final BookingRepository bookingRepository;


    @Override
    public CustomerFullDto getCustomerFullDto(Long id) {
        CustomerDto customer = customerClient.findCustomerById(id);
        List<BookingDto> bookingDtos = bookingRepository.findBookingsByCustomerId(id)
                .stream()
                .map(this::bookingToBookingDto).toList();

        return CustomerFullDto.builder()
                .id(customer.getId())
                .name(customer.getName())
                .bookings(bookingDtos)
                .build();
    }

    @Override
    public List<CustomerFullDto> getAllCustomers() {
        return customerClient.getAllCustomers()
                .stream()
                .map(customerDto -> getCustomerFullDto(customerDto.getId()))
                .toList();
    }

    @Override
    public CustomerDto addCustomer(String name) {
        CustomerDto customer = customerClient.addCustomer(name);
        log.atInfo()
                .addKeyValue("id",customer.getId())
                .log("Customer added successfully");
        return customer;
    }

    @Override
    public void updateCustomerName(Long id, String newName) {
        customerClient.updateCustomerName(id, newName);
        log.atInfo()
                .addKeyValue("id", id)
                .log("Customer name changed successfully");
    }

    @Override
    public CustomerDto findCustomerById(Long id) {
        return customerClient.findCustomerById(id);
    }

    @Override
    public void deleteCustomer (Long customerId){

        boolean hasBooking = bookingRepository.existsByCustomerId(customerId);

        if (hasBooking){
            throw new IllegalArgumentException();
        }

        customerClient.deleteCustomer(customerId);
        log.atInfo()
                .addKeyValue("id", customerId)
                .log("Customer successfully deleted");
    }

    private BookingDto bookingToBookingDto(Booking booking) {
        return BookingDto.builder().id(booking.getId()).customerId(booking.getCustomerId())
                .roomId(booking.getRoom().getId()).checkInDate(booking.getCheckInDate())
                .checkOutDate(booking.getCheckOutDate()).numberOfGuests(booking.getNumberOfGuests())
                .extraBeds(booking.getExtraBeds()).build();
    }
}
