package org.example.pensionatkademina;


import org.example.pensionatkademina.dto.RoomDetailedDto;
import org.example.pensionatkademina.dto.RoomReservationDto;
import org.example.pensionatkademina.model.Booking;
import org.example.pensionatkademina.model.Customer;
import org.example.pensionatkademina.model.Room;
import org.example.pensionatkademina.repository.BookingRepository;
import org.example.pensionatkademina.repository.CustomerRepository;
import org.example.pensionatkademina.repository.RoomRepository;
import org.example.pensionatkademina.service.RoomService;
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

@SpringBootTest
public class RoomServiceTests {

    @Autowired
    private RoomRepository roomTestRepository;

    @Autowired
    private CustomerRepository customerTestRepository;

    @Autowired
    private BookingRepository bookingTestRepository;

    @Autowired
    private RoomService roomTestService;

    private Room savedRoom;

    private final List<Booking> savedBookings = new ArrayList<>();


    @BeforeEach
    public void setup() {

        savedRoom = roomTestRepository.save(Room.builder().type(RoomType.SINGLE).size(RoomSize.SMALL).build());
        roomTestRepository.save(Room.builder().type(RoomType.SINGLE).size(RoomSize.SMALL).build());
        roomTestRepository.save(Room.builder().type(RoomType.DOUBLE).size(RoomSize.SMALL).build());
        roomTestRepository.save(Room.builder().type(RoomType.DOUBLE).size(RoomSize.LARGE).build());

        Customer gabriel = customerTestRepository.save(Customer.builder().name("Gabriel").build());
        customerTestRepository.save(Customer.builder().name("Filip").build());
        customerTestRepository.save(Customer.builder().name("Simon").build());
        customerTestRepository.save(Customer.builder().name("Raul").build());

        savedBookings.add(bookingTestRepository.save(Booking.builder().customer(gabriel)
                .room(savedRoom)
                .checkInDate(LocalDate.of(2026, 5, 25))
                .checkOutDate(LocalDate.of(2026, 5, 26))
                .numberOfGuests(1)
                .extraBeds(0)
                .build()));
    }

    @AfterEach
    public void tearDown() {
        bookingTestRepository.deleteAll();
        customerTestRepository.deleteAll();
        roomTestRepository.deleteAll();
    }


    @Test
    public void roomServiceTest() {
        assertThat(roomTestService).isNotNull();
    }

    @Test
    public void roomToRoomDto() {

        RoomDetailedDto savedDto = roomTestService.roomToRoomDto(savedRoom);

        assertThat(savedDto).isNotNull();
        assertThat(roomTestService.existsById(savedRoom.getId())).isTrue();
        assertThat(savedDto.getType()).isEqualTo(RoomType.SINGLE);
        assertThat(savedDto.getSize()).isEqualTo(RoomSize.SMALL);
        assertThat(savedDto.getRoomReservations()).isNotNull();

        for (RoomReservationDto resDto : savedDto.getRoomReservations()) {
            assertThat(resDto.getCustomerName()).isEqualTo("Gabriel");
            assertThat(resDto.getCheckInDate()).isEqualTo(LocalDate.of(2026, 5, 25));
            assertThat(resDto.getCheckOutDate()).isEqualTo(LocalDate.of(2026, 5, 26));
            assertThat(resDto.getNumberOfGuests()).isEqualTo(1);
            assertThat(resDto.getExtraBeds()).isEqualTo(0);
        }
    }

    @Test
    void bookingsToReservations() {

        List<RoomReservationDto> savedReservations = roomTestService.bookingsToReservations(savedBookings);

        assertThat(savedReservations).isNotNull();
        for (RoomReservationDto reservation : savedReservations) {
            assertThat(reservation.getCustomerName()).isEqualTo("Gabriel");
            assertThat(reservation.getCheckInDate()).isEqualTo(LocalDate.of(2026, 5, 25));
            assertThat(reservation.getCheckOutDate()).isEqualTo(LocalDate.of(2026, 5, 26));
            assertThat(reservation.getNumberOfGuests()).isEqualTo(1);
        }

    }

    @Test
    void getAllRoom() {
        List<RoomDetailedDto> allRooms = roomTestService.getAllRoom();
        assertThat(allRooms).isNotNull();
        assertThat(allRooms.size()).isGreaterThan(1);
    }

    @Test
    void existsById() {
        assertThat(roomTestService.existsById(savedRoom.getId())).isTrue();
    }


}
