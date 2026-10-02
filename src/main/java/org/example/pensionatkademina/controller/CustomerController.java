package org.example.pensionatkademina.controller;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.pensionatkademina.dto.CustomerFullDto;
import org.example.pensionatkademina.service.imp.CustomerServiceImp;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.HtmlUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(path = "customer")
public class CustomerController {

    private final CustomerServiceImp customerService;

    @RequestMapping
    public String allCustomers(Model model) {
        List<CustomerFullDto> customerList;
        try {
            customerList = customerService.getAllCustomers();
        } catch (ResourceAccessException e) {
            log.error("Service is not available", e);
            customerList = new ArrayList<>();
            model.addAttribute("errorMessage",
                    "The page failed to load properly. Customer Service is currently unavailable" );
        }
        model.addAttribute("allCustomers", customerList);
        return "customer";
    }

    @PostMapping("add")
    public String addCustomer(@RequestParam @NotNull
                              @Size(min = 2, max = 20) @Pattern(regexp = "^[A-Za-z-]+$") String name,
                              RedirectAttributes redirectAttributes) {
        try {
            customerService.addCustomer(name);
            redirectAttributes.addFlashAttribute(
                    "message", "Customer <strong>" +
                            HtmlUtils.htmlEscape(name) + "</strong> added.");
        } catch (ResourceAccessException e) {
            log.error("Service is not available", e);
            redirectAttributes.addFlashAttribute("errorMessage",
                    "The page failed to load properly. Customer Service is currently unavailable" );
        }

        return "redirect:/customer";
    }

    @PostMapping("edit/{id}")
    public String editCustomer(@PathVariable Long id, @RequestParam @NotNull
                               @Size(min = 2, max = 20) @Pattern(regexp = "^[A-Za-z-]+$") String name,
                               RedirectAttributes redirectAttributes) {
        customerService.updateCustomerName(id, name);
        redirectAttributes.addFlashAttribute(
                "message",
                "Customer name updated to <strong>" +
                        HtmlUtils.htmlEscape(name) + "</strong>.");
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
            log.warn("Cannot delete customer with active booking", e);
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Failed to delete. Customer <strong>" + HtmlUtils.htmlEscape(name) + "</strong> has an active booking.");
        }
        return "redirect:/customer";
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public String handleValidationException(RedirectAttributes redirectAttributes, Exception e) {
        redirectAttributes.addFlashAttribute("errorMessage",
                "Name must be at least 2 characters long and can only contain letters and dashes.");
        log.warn("Name not properly filled out", e);
        return "redirect:/customer";
    }

    @ExceptionHandler ({DataAccessException.class, CannotCreateTransactionException.class})
    public String handleDbUnavailable(Exception e, RedirectAttributes redirectAttributes) {
        String message = "Database is currently unavailable";
        log.error(message, e);
        redirectAttributes.addFlashAttribute("errorMessage",
                message);
        return "redirect:/customer";
    }
}
