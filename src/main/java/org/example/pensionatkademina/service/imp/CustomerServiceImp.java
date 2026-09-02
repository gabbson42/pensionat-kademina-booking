package org.example.pensionatkademina.service.imp;

import jakarta.transaction.Transactional;
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
    @Transactional
    public CustomerFullDto customerToCustomerFullDto(Long id) {
        Customer customer = customerClient.findById(id).orElseThrow();
        List<BookingDto> bookingDtos = customer.getBookings().stream()
                .map(this::bookingToBookingDto).toList();

        return CustomerFullDto.builder()
                .id(customer.getId())
                .name(customer.getName())
                .bookings(bookingDtos)
                .build();
    }

    @Override
    public List<CustomerFullDto> getAllCustomers(){

        List<CustomerDto> customerList = customerClient.getallCustomers();


        return customerClient.getallCustomers();
    }

    @Override
    public void addCustomer(CustomerDto customerDto) {
        Customer customer = customerDtoToCustomer(customerDto);
        customerClient.save(customer);
    }

    @Override
    public void updateCustomerName(Long id, String newName) {
        Customer customer = customerClient.findById(id).orElseThrow();
        customer.setName(newName);
        customerClient.save(customer);
    }

    @Override
    public CustomerDto findCustomerById(Long id) {
        return customerToCustomerDto(customerClient.findById(id).orElseThrow());
    }

    @Override
    public void deleteCustomer (Long customerId){

        boolean hasBooking = bookingRepository.existsByCustomer_Id(customerId);

        if (hasBooking){
            throw new IllegalArgumentException();
        }

        customerClient.deleteById(customerId);
    }

    private BookingDto bookingToBookingDto(Booking booking) {
        return BookingDto.builder().id(booking.getId()).customerId(booking.getCustomerId())
                .roomId(booking.getRoom().getId()).checkInDate(booking.getCheckInDate())
                .checkOutDate(booking.getCheckOutDate()).numberOfGuests(booking.getNumberOfGuests())
                .extraBeds(booking.getExtraBeds()).build();
    }
}
