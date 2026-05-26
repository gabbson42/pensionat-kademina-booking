package org.example.pensionatkademina;

import jakarta.transaction.Transactional;
import org.example.pensionatkademina.dto.BookingDto;
import org.example.pensionatkademina.dto.CustomerDto;
import org.example.pensionatkademina.dto.CustomerFullDto;
import org.example.pensionatkademina.model.Booking;
import org.example.pensionatkademina.model.Customer;
import org.example.pensionatkademina.model.Room;
import org.example.pensionatkademina.repository.BookingRepository;
import org.example.pensionatkademina.repository.CustomerRepository;
import org.example.pensionatkademina.repository.RoomRepository;
import org.example.pensionatkademina.service.imp.CustomerServiceImp;
import org.example.pensionatkademina.utility.RoomSize;
import org.example.pensionatkademina.utility.RoomType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class CustomerServiceTest {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private CustomerServiceImp customerService;

    private Customer customer1;
    private Customer customer2;
    private Customer customer3;
    private Customer customer4;

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

           customer1 = customerRepository.save(Customer.builder().name("Gabriel").build());
           customer2 = customerRepository.save(Customer.builder().name("Filip").build());
           customer3 = customerRepository.save(Customer.builder().name("Simon").build());
           customer4 = customerRepository.save(Customer.builder().name("Raul").build());

            bookingRepository.save(Booking.builder().checkInDate(LocalDate.parse("2026-05-25"))
                    .checkOutDate(LocalDate.parse("2026-05-26")).numberOfGuests(1)
                    .customer(customerRepository.findAll().getFirst())
                    .room(roomRepository.findAll().getFirst()).build());
    }

    @AfterEach
    void tearDown() {
        bookingRepository.deleteAll();
        customerRepository.deleteAll();
        roomRepository.deleteAll();
    }

    @Test
    void customerToCustomerDtoTest() {
        CustomerDto customerDto = customerService
                .customerToCustomerDto(customerRepository.findAll().getFirst());

        assertNotNull(customerDto);
        assertThat(customerDto.getName()).isEqualTo("Gabriel");
    }

    @Test
    void customerDtoToCustomerTest() {
        CustomerDto customerDto = CustomerDto.builder().id(1L).name("Gabriel").build();
        Customer customer = customerService.customerDtoToCustomer(customerDto);

        assertNotNull(customer);
        assertThat(customer.getId() == 1L);
        assertThat(customer.getName().equals("Gabriel"));
    }

    @Test
    @Transactional
    void customerToCustomerFullDtoTest() {
        CustomerFullDto customerFullDto = customerService
                .customerToCustomerFullDto(customerRepository.findAll().getFirst());

        assertNotNull(customerFullDto);
        assertThat(customerFullDto.getName().equals("Gabriel"));
        assertThat(customerFullDto.getBookings().size() == 1);
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
