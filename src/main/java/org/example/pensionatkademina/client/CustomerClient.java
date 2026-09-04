package org.example.pensionatkademina.client;

import org.example.pensionatkademina.dto.CustomerDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class CustomerClient {

    private final RestClient restClient;

    public CustomerClient (RestClient customerRestClient) {
        this.restClient = customerRestClient;
    }

    public List<CustomerDto> getAllCustomers() {
        return restClient.get()
                .uri("customer")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    public CustomerDto findCustomerById(Long id) {
        return restClient.get()
                .uri("customer/{id}", id)
                .retrieve()
                .body(CustomerDto.class);
    }

    public CustomerDto addCustomer(String name) {
        return restClient.post()
                .uri("customer/add")
                .contentType(MediaType.APPLICATION_JSON)
                .body(name)
                .retrieve()
                .body(CustomerDto.class);
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

    public void deleteCustomer (Long customerId){
        restClient.post()
                .uri("customer/delete/{id}", customerId)
                .retrieve()
                .toBodilessEntity();
    }
}
