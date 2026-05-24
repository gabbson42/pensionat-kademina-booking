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

    @PostMapping("add")
    public String addCustomer(@RequestParam String name) {
        customerService.addCustomer(CustomerDto.builder().name(name).build());
        return "redirect:/customer";
    }

    @RequestMapping("editName/{id}")
    public String changeCustomerName(@PathVariable Long id, Model model) {
        CustomerDto customerDto = customerService.findCustomerById(id);
        model.addAttribute("customer", customerDto);
        return "updateCustomer";
    }

    @PostMapping("update")
    public String updateCustomer(CustomerDto customerDto){
        customerService.addCustomer(customerDto);
        return "redirect:/customer";
    }

    @RequestMapping("delete/{id}")
    public String deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return "redirect:/customer";
    }


}
