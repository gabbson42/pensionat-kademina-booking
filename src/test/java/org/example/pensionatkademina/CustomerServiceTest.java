package org.example.pensionatkademina;

import jakarta.transaction.Transactional;
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

    @BeforeEach
    void setUp() {

        roomRepository.save(
                new Room(null, RoomType.SINGLE, RoomSize.SMALL, new ArrayList<>())
        );
        roomRepository.save(
                new Room(null, RoomType.DOUBLE, RoomSize.SMALL, new ArrayList<>())
        );
        roomRepository.save(
                new Room(null, RoomType.DOUBLE, RoomSize.LARGE, new ArrayList<>())
        );
        roomRepository.save(
                new Room(null, RoomType.SINGLE, RoomSize.SMALL, new ArrayList<>())
        );

        customerRepository.save(Customer.builder().name("Gabriel").build());
        customerRepository.save(Customer.builder().name("Filip").build());
        customerRepository.save(Customer.builder().name("Simon").build());
        customerRepository.save(Customer.builder().name("Raul").build());

        bookingRepository.save(Booking.builder().checkInDate(LocalDate.parse("2026-05-25"))
                .checkOutDate(LocalDate.parse("2026-05-26")).numberOfGuests(1)
                .customer(customerRepository.findAll().getFirst())
                .room(roomRepository.findAll().getFirst()).build());
    }

    @AfterEach
    public void tearDown() {
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
        assertThat(customer.getId()).isEqualTo(1L);
        assertThat(customer.getName()).isEqualTo("Gabriel");
    }

    @Test
    void customerToCustomerFullDtoTest() {
        CustomerFullDto customerFullDto = customerService
                .customerToCustomerFullDto(customerRepository.findAll().getFirst().getId());

        assertNotNull(customerFullDto);
        assertThat(customerFullDto.getName()).isEqualTo("Gabriel");
        assertThat(customerFullDto.getBookings()).hasSize(1);
    }

    @Test
    void getAllCustomersTest() {
        List<CustomerFullDto> customerList = customerService.getAllCustomers();

        assertNotNull(customerList);
        assertThat(customerList).hasSize(4);
        assertThat(customerList.getFirst().getName()).isEqualTo("Gabriel");
        assertThat(customerList.getLast().getName()).isEqualTo("Raul");
        assertThat(customerList.getFirst().getBookings()).hasSize(1);
    }

    @Test
    void addCustomerTest() {
        customerService.addCustomer(CustomerDto.builder().name("Test").build());
        Customer customer = customerRepository.findCustomerByName("Test");

        assertNotNull(customer);
        assertThat(customer.getName()).isEqualTo("Test");
    }

    @Test
    void updateCustomerNameTest() {
        Customer customer = customerRepository.findAll().getFirst();
        Long id = customer.getId();
        customerService.updateCustomerName(id, "Test");
        Customer test = customerRepository.findById(id).orElseThrow();
        assertThat(test.getName()).isEqualTo("Test");
    }

    @Test
    void findCustomerByIdTest() {
        Customer customer = customerRepository.findAll().getFirst();
        Long id = customer.getId();
        CustomerDto customerDto = customerService.findCustomerById(id);

        assertNotNull(customerDto);
        assertThat(customerDto.getName()).isEqualTo("Gabriel");
    }

    @Test
    void deleteCustomerTest() {
        Customer customer = customerRepository.findAll().getLast();
        Long id = customer.getId();

        customerService.deleteCustomer(id);

        assertThat(customerRepository.findById(id)).isEmpty();
    }
}
