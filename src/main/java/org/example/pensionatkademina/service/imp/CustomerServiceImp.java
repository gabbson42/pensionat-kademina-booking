package org.example.pensionatkademina.service.imp;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.pensionatkademina.dto.BookingDto;
import org.example.pensionatkademina.dto.CustomerDto;
import org.example.pensionatkademina.dto.CustomerFullDto;
import org.example.pensionatkademina.model.Booking;
import org.example.pensionatkademina.model.Customer;
import org.example.pensionatkademina.repository.BookingRepository;
import org.example.pensionatkademina.repository.CustomerRepository;
import org.example.pensionatkademina.repository.RoomRepository;
import org.example.pensionatkademina.service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImp implements CustomerService {

    private final CustomerRepository customerRepository;
    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;

    @Override
    public CustomerDto customerToCustomerDto(Customer customer) {
        return CustomerDto.builder().id(customer.getId()).name(customer.getName()).build();
    }

    @Override
    public Customer customerDtoToCustomer(CustomerDto customerDto) {
        return Customer.builder().id(customerDto.getId()).name(customerDto.getName()).build();
    }

    @Override
    public CustomerFullDto customerToCustomerFullDto(Customer customer) {
        List<BookingDto> bookingDtos = customer.getBookings().stream()
                .map(this::bookingToBookingDto).toList();

        return CustomerFullDto.builder()
                .id(customer.getId())
                .name(customer.getName())
                .bookings(bookingDtos)
                .build();
    }

    @Override
    public Customer customerFullDtoToCustomer(CustomerFullDto customerFullDto) {
        List<Booking> bookings = customerFullDto.getBookings().stream()
                .map(this::bookingDtoToBooking).toList();
        return Customer.builder().id(customerFullDto.getId()).name(customerFullDto.getName())
                .bookings(bookings).build();
    }

    @Override
    @Transactional
    public List<CustomerFullDto> getAllCustomers(){
        return customerRepository.findAll().stream().map(this::customerToCustomerFullDto).toList();
    }

    @Override
    public void addCustomer(CustomerDto customerDto) {
        Customer customer = customerDtoToCustomer(customerDto);
        customerRepository.save(customer);
    }

    @Override
    public void updateCustomerName(Long id, String newName) {
        Customer customer = customerRepository.findById(id).orElseThrow();
        customer.setName(newName);
        customerRepository.save(customer);
    }

    @Override
    public CustomerDto findCustomerByName(String name) {
        return customerToCustomerDto(customerRepository.findCustomerByName(name));
    }

    @Override
    public CustomerDto findCustomerById(Long id) {
        return customerToCustomerDto(customerRepository.findById(id).orElseThrow());
    }

    public void deleteCustomer (Long customerId){

        boolean hasBooking = bookingRepository.existsByCustomer_Id(customerId);

        if (hasBooking){
            throw new IllegalArgumentException();
        }

        customerRepository.deleteById(customerId);
    }

    private BookingDto bookingToBookingDto(Booking booking) {
        return BookingDto.builder().id(booking.getId()).customerId(booking.getCustomer().getId())
                .roomId(booking.getRoom().getId()).checkInDate(booking.getCheckInDate())
                .checkOutDate(booking.getCheckOutDate()).numberOfGuests(booking.getNumberOfGuests()).build();
    }

    private Booking bookingDtoToBooking(BookingDto bookingDto) {
        return Booking.builder().id(bookingDto.getId())
                .customer(customerDtoToCustomer(findCustomerById(bookingDto.getCustomerId())))
                .room(roomRepository.getRoomById(bookingDto.getRoomId()))
                .checkInDate(bookingDto.getCheckInDate())
                .checkOutDate(bookingDto.getCheckOutDate())
                .numberOfGuests(bookingDto.getNumberOfGuests()).build();
    }
}
