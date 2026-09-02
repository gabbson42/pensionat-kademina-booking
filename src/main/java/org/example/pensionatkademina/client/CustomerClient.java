package org.example.pensionatkademina.client;

import org.example.pensionatkademina.dto.CustomerDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerClient {

    RestTemplate restTemplate;


    public List<CustomerDto> getAllCustomers() {

        ParameterizedTypeReference<List<CustomerDto>> typeRef = new ParameterizedTypeReference<>() {};

        ResponseEntity<List<CustomerDto>> response = restTemplate.exchange(
                "http://localhost:8081/customer",
                HttpMethod.GET,
                null,
                typeRef);

        if(response.getBody() != null) {
            return response.getBody();
        }
        return new ArrayList<>();

    }

    public CustomerDto findCustomerById(Long id) {
        throw new RuntimeException();
    }

    public void addCustomer(CustomerDto customerDto) {

    }

    public void updateCustomerName(Long id, String newName) {
    }

    public void deleteCustomer (Long customerId){
    }



}
