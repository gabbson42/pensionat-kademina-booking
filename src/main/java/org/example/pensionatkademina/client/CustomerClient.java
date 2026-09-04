package org.example.pensionatkademina.client;

import org.example.pensionatkademina.dto.CustomerDto;
import org.example.pensionatkademina.exception.ServiceUnavailableException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerClient {

    private final RestClient restClient;

    public CustomerClient(RestClient customerRestClient) {
        this.restClient = customerRestClient;
    }

    public List<CustomerDto> getAllCustomers() {
        try {
            return restClient.get()
                    .uri("customer")
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });
        } catch (ResourceAccessException e) {
            throw new ResourceAccessException("Service is currently down");
        }

    }

    public CustomerDto findCustomerById(Long id) {
        try {
            return restClient.get()
                    .uri("customer/{id}", id)
                    .retrieve()
                    .body(CustomerDto.class);
        } catch (ResourceAccessException e) {
            throw new ResourceAccessException("Service is currently down");
        }

    }

    public CustomerDto addCustomer(String name) {
        try {
            return restClient.post()
                    .uri("customer/add")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(name)
                    .retrieve()
                    .body(CustomerDto.class);
        } catch (ResourceAccessException e) {
            throw new ResourceAccessException("Service is currently down");
        }
    }

    public void updateCustomerName(Long id, String newName) {
        CustomerDto customerDto = CustomerDto.builder().name(newName).id(id).build();

        restClient.post()
                .uri("customer/edit/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .body(customerDto)
                .retrieve()
                .toBodilessEntity();
    }

    public void deleteCustomer(Long customerId) {
        restClient.post()
                .uri("customer/delete/{id}", customerId)
                .retrieve()
                .toBodilessEntity();
    }
}
