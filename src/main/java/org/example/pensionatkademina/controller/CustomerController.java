package org.example.pensionatkademina.controller;

import org.example.pensionatkademina.dto.CustomerDto;
import org.example.pensionatkademina.service.imp.CustomerServiceImp;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CustomerController {

    private CustomerServiceImp customerServiceImp;

    @RequestMapping
    public List<CustomerDto> getAllCustomers


}
