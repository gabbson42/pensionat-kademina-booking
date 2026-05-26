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
    private RoomDetailedDto savedDto;

    private Booking savedBooking;
    private List<Booking> savedBookings = new ArrayList<>();
    private List<RoomReservationDto> savedReservations = new ArrayList<>();




    @BeforeEach
    public void setup() {

        savedRoom = roomTestRepository.save(Room.builder().type(RoomType.SINGLE).size(RoomSize.SMALL).extraBeds(0).build());
        roomTestRepository.save(Room.builder().type(RoomType.SINGLE).size(RoomSize.SMALL).extraBeds(0).build());
        roomTestRepository.save(Room.builder().type(RoomType.DOUBLE).size(RoomSize.SMALL).extraBeds(0).build());
        roomTestRepository.save(Room.builder().type(RoomType.DOUBLE).size(RoomSize.LARGE).extraBeds(0).build());

        Customer gabriel = customerTestRepository.save(Customer.builder().name("Gabriel").build());
        customerTestRepository.save(Customer.builder().name("Filip").build());
        customerTestRepository.save(Customer.builder().name("Simon").build());
        customerTestRepository.save(Customer.builder().name("Raul").build());

        savedBooking = bookingTestRepository.save(Booking.builder().customer(gabriel)
                .room(savedRoom)
                .checkInDate(LocalDate.of(2026, 5, 25))
                .checkOutDate(LocalDate.of(2026, 5, 26))
                .numberOfGuests(1)
                .build());

        savedDto = roomTestService.roomToRoomDto(savedRoom);

        savedBookings.add(savedBooking);

        savedReservations = roomTestService.bookingsToReservations(savedBookings);
    }

    @AfterEach
    public void tearDown() {
        bookingTestRepository.deleteAll();
        roomTestRepository.deleteAll();
        customerTestRepository.deleteAll();
    }


    @Test
    public void roomServiceTest() throws Exception {
        assertThat(roomTestService).isNotNull();
    }

    @Test
    public void roomToRoomDto() throws Exception {

        savedDto = roomTestService.roomToRoomDto(savedRoom);

        assertThat(savedDto).isNotNull();
        assertThat(roomTestService.existsById(savedRoom.getId())).isTrue();
        assertThat(savedDto.getType()).isEqualTo(RoomType.SINGLE);
        assertThat(savedDto.getSize()).isEqualTo(RoomSize.SMALL);
        assertThat(savedDto.getExtraBeds()).isEqualTo(0);
        assertThat(savedDto.getRoomReservations()).isNotNull();

        for (RoomReservationDto resDto : savedDto.getRoomReservations()) {
            assertThat(resDto.getCustomerName()).isEqualTo("Gabriel");
            assertThat(resDto.getCheckInDate()).isEqualTo(LocalDate.of(2026, 5, 25));
            assertThat(resDto.getCheckOutDate()).isEqualTo(LocalDate.of(2026, 5, 26));
            assertThat(resDto.getNumberOfGuests()).isEqualTo(1);
        }
    }

    @Test
    public void roomDtoToRoom() throws Exception {

        Room room = roomTestService.roomDtoToRoom(savedDto);

        assertThat(room).isNotNull();
        assertThat(room.getType()).isEqualTo(RoomType.SINGLE);
        assertThat(room.getSize()).isEqualTo(RoomSize.SMALL);
        assertThat(room.getExtraBeds()).isEqualTo(0);
        assertThat(room.getBooking()).isNotNull();

        for (Booking booking : room.getBooking()) {
            assertThat(booking.getCustomer().getName()).isEqualTo("Gabriel");
            assertThat(booking.getCheckInDate()).isEqualTo(LocalDate.of(2026, 5, 25));
            assertThat(booking.getCheckOutDate()).isEqualTo(LocalDate.of(2026, 5, 26));
            assertThat(booking.getNumberOfGuests()).isEqualTo(1);
        }

    }

    @Test
    void bookingsToReservations() {

        savedReservations = roomTestService.bookingsToReservations(savedBookings);

        assertThat(savedReservations).isNotNull();
        for (RoomReservationDto reservation : savedReservations) {
            assertThat(reservation.getCustomerName()).isEqualTo("Gabriel");
            assertThat(reservation.getCheckInDate()).isEqualTo(LocalDate.of(2026, 5, 25));
            assertThat(reservation.getCheckOutDate()).isEqualTo(LocalDate.of(2026, 5, 26));
            assertThat(reservation.getNumberOfGuests()).isEqualTo(1);
        }

    }

    @Test
    void reservationsToBookings() {

        List<Booking> bookings = roomTestService.reservationsToBookings(savedReservations);

        assertThat(bookings).isNotNull();
        for (Booking booking : bookings){
            assertThat(booking.getCustomer().getName()).isEqualTo("Gabriel");
            assertThat(booking.getCheckInDate()).isEqualTo(LocalDate.of(2026, 5, 25));
            assertThat(booking.getCheckOutDate()).isEqualTo(LocalDate.of(2026, 5, 26));
            assertThat(booking.getNumberOfGuests()).isEqualTo(1);
        }
    }

    @Test
    void getAllRoom() {
    }

    @Test
    void addRoom() {
    }

    @Test
    void setExtraBeds() {
    }

    @Test
    void findById() {
    }

    @Test
    void existsById() {
    }


}
