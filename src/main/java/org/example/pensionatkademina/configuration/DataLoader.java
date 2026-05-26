package org.example.pensionatkademina.configuration;

import lombok.RequiredArgsConstructor;
import org.example.pensionatkademina.model.Booking;
import org.example.pensionatkademina.model.Customer;
import org.example.pensionatkademina.model.Room;
import org.example.pensionatkademina.repository.BookingRepository;
import org.example.pensionatkademina.repository.CustomerRepository;
import org.example.pensionatkademina.repository.RoomRepository;
import org.example.pensionatkademina.utility.RoomSize;
import org.example.pensionatkademina.utility.RoomType;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.ArrayList;

@Configuration
@RequiredArgsConstructor
public class DataLoader {

    private final RoomRepository roomRepository;
    private final CustomerRepository customerRepository;
    private final BookingRepository bookingRepository;

    @Bean
    CommandLineRunner loadData() {
        return args -> {

            if (roomRepository.count() == 0) {

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
            }

            if (customerRepository.count() == 0) {
                customerRepository.save(Customer.builder().name("Gabriel").build());
                customerRepository.save(Customer.builder().name("Filip").build());
                customerRepository.save(Customer.builder().name("Simon").build());
                customerRepository.save(Customer.builder().name("Raul").build());
            }

            if (bookingRepository.count() == 0) {
                bookingRepository.save(Booking.builder().checkInDate(LocalDate.parse("2026-05-25"))
                        .checkOutDate(LocalDate.parse("2026-05-26")).numberOfGuests(1)
                        .customer(customerRepository.findCustomerByName("Gabriel"))
                        .room(roomRepository.findAll().getFirst()).build());
            }
        };
    }
}
