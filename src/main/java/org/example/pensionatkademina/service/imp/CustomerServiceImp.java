package org.example.pensionatkademina.service.imp;

import lombok.RequiredArgsConstructor;
import org.example.pensionatkademina.dto.CustomerDto;
import org.example.pensionatkademina.dto.CustomerFullDto;
import org.example.pensionatkademina.model.Customer;
import org.example.pensionatkademina.repository.BookingRepository;
import org.example.pensionatkademina.repository.CustomerRepository;
import org.example.pensionatkademina.service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImp implements CustomerService {

    private final CustomerRepository customerRepository;
    private final BookingRepository bookingRepository;

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
        return CustomerFullDto.builder().id(customer.getId()).name(customer.getName())
                .bookings(customer.getBookings()).build();
    }

    @Override
    public Customer customerFullDtoToCustomer(CustomerFullDto customerFullDto) {
        return Customer.builder().id(customerFullDto.getId()).name(customerFullDto.getName())
                .bookings(customerFullDto.getBookings()).build();
    }

    @Override
    public List<CustomerDto> getAllCustomers(){
        return customerRepository.findAll().stream().map(this::customerToCustomerDto).toList();
    }

    @Override
    public void addCustomer(CustomerDto customerDto) {
        Customer customer = customerDtoToCustomer(customerDto);
        customerRepository.save(customer);
    }

    @Override
    public void updateCustomerName(String newName, String oldName) {
        customerRepository.updateCustomerName(newName, oldName);
    }

    @Override
    public CustomerDto findCustomerByName(String name) {
        return customerToCustomerDto(customerRepository.findCustomerByName(name));
    }

    public void deleteCustomer (Long customerId){

        boolean hasBooking = bookingRepository.existsByCustomer_Id(customerId);

        if (hasBooking){
            throw new IllegalArgumentException("Customer has booking");
        }

        customerRepository.deleteById(customerId);
    }
}
