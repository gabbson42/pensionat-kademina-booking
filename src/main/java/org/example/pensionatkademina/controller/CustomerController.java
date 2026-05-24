package org.example.pensionatkademina.controller;

import lombok.RequiredArgsConstructor;
import org.example.pensionatkademina.dto.CustomerDto;
import org.example.pensionatkademina.service.imp.CustomerServiceImp;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "customer")
public class CustomerController {

    private final CustomerServiceImp customerService;

    @RequestMapping
    public String allCustomers(Model model) {
        List<CustomerDto> customerList = customerService.getAllCustomers();
        model.addAttribute("allCustomers", customerList);
        return "customer";
    }

    @RequestMapping("add")
    public List<CustomerDto> addCustomer(@RequestBody CustomerDto customerDto) {
        customerService.addCustomer(customerDto);
        return customerService.getAllCustomers();
    }

    @RequestMapping("editName/{id}")
    public String changeCustomerName(@PathVariable Long id, Model model) {
        CustomerDto customerDto = customerService.findCustomerById(id);
        model.addAttribute("customer", customerDto);
        return "updateCustomer";
    }

    @PostMapping("update")
    public String updateCustomer(CustomerDto customerDto, Model model){
        customerService.addCustomer(customerDto);
        List<CustomerDto> customerList = customerService.getAllCustomers();
        model.addAttribute("allCustomers", customerList);
        return "customer";
    }

    @RequestMapping("delete/{id}")
    public String deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return "redirect:/customer";
    }


}
