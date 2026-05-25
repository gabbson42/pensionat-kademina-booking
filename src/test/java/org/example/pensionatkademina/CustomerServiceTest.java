package org.example.pensionatkademina;

import org.example.pensionatkademina.model.Booking;
import org.example.pensionatkademina.model.Customer;
import org.example.pensionatkademina.model.Room;
import org.example.pensionatkademina.repository.BookingRepository;
import org.example.pensionatkademina.repository.CustomerRepository;
import org.example.pensionatkademina.repository.RoomRepository;
import org.example.pensionatkademina.utility.RoomSize;
import org.example.pensionatkademina.utility.RoomType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;

@SpringBootTest
public class CustomerServiceTest {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private BookingRepository bookingRepository;

    @BeforeEach
    void setUp() {
            roomRepository.save(
                    new Room(null, RoomType.SINGLE , RoomSize.SMALL, 0, new ArrayList<>())
            );
            roomRepository.save(
                    new Room(null,RoomType.DOUBLE,  RoomSize.SMALL, 0,new ArrayList<>())
            );
            roomRepository.save(
                    new Room(null,RoomType.DOUBLE, RoomSize.LARGE, 0, new ArrayList<>())
            );
            roomRepository.save(
                    new Room(null, RoomType.SINGLE, RoomSize.SMALL, 0, new ArrayList<>())
            );

            customerRepository.save(Customer.builder().name("Gabriel").build());
            customerRepository.save(Customer.builder().name("Filip").build());
            customerRepository.save(Customer.builder().name("Simon").build());
            customerRepository.save(Customer.builder().name("Raul").build());

            bookingRepository.save(Booking.builder().checkInDate(LocalDate.parse("2026-05-25"))
                    .checkOutDate(LocalDate.parse("2026-05-26")).numberOfGuests(1)
                    .customer(customerRepository.findCustomerByName("Gabriel"))
                    .room(roomRepository.findById(3L).orElseThrow()).build());
    }

    @AfterEach
    void tearDown() {
        customerRepository.deleteAll();
        roomRepository.deleteAll();
        bookingRepository.deleteAll();
    }

    @Test
    void customerToCustomerDtoTest() {
    }

    @Test
    void customerDtoToCustomerTest() {
    }

    @Test
    void customerToCustomerFullDtoTest() {
    }

    @Test
    void customerFullDtoToCustomerTest() {
    }

    @Test
    void getAllCustomersTest() {
    }

    @Test
    void addCustomerTest() {
    }

    @Test
    void updateCustomerNameTest() {
    }

    @Test
    void findCustomerByIdTest() {
    }

    @Test
    void deleteCustomerTest() {
    }
}
