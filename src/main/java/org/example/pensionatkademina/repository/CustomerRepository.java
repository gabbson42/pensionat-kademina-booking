package org.example.pensionatkademina.repository;

import org.example.pensionatkademina.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
