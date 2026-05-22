package org.example.pensionatkademina.repository;

import org.example.pensionatkademina.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking,Long> {

    boolean existsByCustomer_Id(Long customerId);


}
