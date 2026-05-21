package org.example.pensionatkademina.repository;

import jakarta.transaction.Transactional;
import org.example.pensionatkademina.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer findCustomerByName(String name);

    @Modifying
    @Transactional
    @Query("UPDATE Customer SET name=?1 WHERE name=?2")
    void updateCustomerName(@Param("newName") String newName,
                    @Param("oldName") String oldName);
}
