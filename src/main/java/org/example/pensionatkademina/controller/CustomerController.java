package org.example.pensionatkademina.controller;

import lombok.RequiredArgsConstructor;
import org.example.pensionatkademina.dto.CustomerDto;
import org.example.pensionatkademina.service.imp.CustomerServiceImp;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("customer")
public class CustomerController {

    private final CustomerServiceImp customerService;

    @RequestMapping("all")
    public List<CustomerDto> allCustomers() {
        return customerService.getAllCustomers();
    }

    @PostMapping("add")
    public List<CustomerDto> addCustomer(@RequestBody CustomerDto customerDto) {
        customerService.addCustomer(customerDto);
        return customerService.getAllCustomers();
    }

    @PutMapping("changeName")
    public CustomerDto changeCustomerName(@RequestBody String newName, String oldName) {
        customerService.updateCustomerName(newName, oldName);
        return customerService.findCustomerByName(newName);
    }


}
