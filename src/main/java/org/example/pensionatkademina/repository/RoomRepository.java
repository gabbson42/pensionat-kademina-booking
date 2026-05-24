package org.example.pensionatkademina.repository;

import org.example.pensionatkademina.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Room getRoomById(Long id);
}
