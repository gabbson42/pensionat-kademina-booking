package org.example.pensionatkademina.service.imp;

import lombok.RequiredArgsConstructor;
import org.example.pensionatkademina.client.CustomerClient;
import org.example.pensionatkademina.dto.BookingDto;
import org.example.pensionatkademina.dto.CustomerDto;
import org.example.pensionatkademina.dto.CustomerFullDto;
import org.example.pensionatkademina.model.Booking;
import org.example.pensionatkademina.repository.BookingRepository;
import org.example.pensionatkademina.service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List<CustomerFullDto> getAllCustomers(){
        return customerClient.getAllCustomers()
                .stream()
                .map(customerDto -> getCustomerFullDto(customerDto.getId()))
                .toList();
    }

    @Override
    public void addCustomer(String name) {
        customerClient.addCustomer(name);
    }

    @Override
    public void updateCustomerName(Long id, String newName) {
        customerClient.updateCustomerName(id, newName);
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
    }

    private BookingDto bookingToBookingDto(Booking booking) {
        return BookingDto.builder().id(booking.getId()).customerId(booking.getCustomerId())
                .roomId(booking.getRoom().getId()).checkInDate(booking.getCheckInDate())
                .checkOutDate(booking.getCheckOutDate()).numberOfGuests(booking.getNumberOfGuests())
                .extraBeds(booking.getExtraBeds()).build();
    }
}
