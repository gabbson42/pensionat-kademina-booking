package org.example.pensionatkademina.controller;

import lombok.RequiredArgsConstructor;
import org.example.pensionatkademina.dto.CustomerDto;
import org.example.pensionatkademina.dto.CustomerFullDto;
import org.example.pensionatkademina.service.imp.CustomerServiceImp;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "customer")
public class CustomerController {

    private final CustomerServiceImp customerService;

    @RequestMapping
    public String allCustomers(Model model) {
        List<CustomerFullDto> customerList = customerService.getAllCustomers();
        model.addAttribute("allCustomers", customerList);
        return "customer";
    }

    @PostMapping("add")
    public String addCustomer(@RequestParam String name, RedirectAttributes redirectAttributes) {
        customerService.addCustomer(CustomerDto.builder().name(name).build());
        redirectAttributes.addFlashAttribute(
                "message", "Customer <strong>" + HtmlUtils.htmlEscape(name) + "</strong> added.");
        return "redirect:/customer";
    }

    @PostMapping("edit/{id}")
    public String editCustomer(@PathVariable Long id, @RequestParam String name,
                               RedirectAttributes redirectAttributes) {
        customerService.updateCustomerName(id, name);
        redirectAttributes.addFlashAttribute(
                "message",
                "Customer name updated to <strong>" + HtmlUtils.htmlEscape(name) + "</strong>.");
        return "redirect:/customer";
    }

    @PostMapping("delete/{id}")
    public String deleteCustomer(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        String name = customerService.findCustomerById(id).getName();
       try {
           customerService.deleteCustomer(id);
           redirectAttributes.addFlashAttribute("message",
                   "Customer <strong>" + HtmlUtils.htmlEscape(name) + "</strong> deleted.");
       } catch (IllegalArgumentException e) {
           redirectAttributes.addFlashAttribute("errorMessage",
                   "Customer <strong>" + HtmlUtils.htmlEscape(name) + "</strong> has an active booking.");
       }
        return "redirect:/customer";
    }
}
