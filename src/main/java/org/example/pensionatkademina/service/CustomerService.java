package org.example.pensionatkademina.service;

import org.example.pensionatkademina.dto.CustomerDto;
import org.example.pensionatkademina.dto.CustomerFullDto;

import java.util.List;

public interface CustomerService {
    CustomerFullDto getCustomerFullDto(Long id);
    List<CustomerFullDto> getAllCustomers();
    void addCustomer(CustomerDto customerDto);
    void updateCustomerName(Long id, String newName);
    CustomerDto findCustomerById(Long id);
    void deleteCustomer (Long customerId);


}
