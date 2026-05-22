package org.example.pensionatkademina.service;

import org.example.pensionatkademina.dto.CustomerDto;
import org.example.pensionatkademina.dto.CustomerFullDto;
import org.example.pensionatkademina.model.Customer;

import java.util.List;

public interface CustomerService {
    CustomerDto customerToCustomerDto(Customer customer);
    Customer customerDtoToCustomer(CustomerDto customerDto);
    CustomerFullDto customerToCustomerFullDto(Customer customer);
    Customer customerFullDtoToCustomer(CustomerFullDto customerFullDto);
    List<CustomerDto> getAllCustomers();
    void addCustomer(CustomerDto customerDto);
    void updateCustomerName(String newName, String oldName);
    CustomerDto findCustomerByName(String name);


}
