package org.example.pensionatkademina.configuration;

import lombok.RequiredArgsConstructor;
import org.example.pensionatkademina.model.Room;
import org.example.pensionatkademina.repository.RoomRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

@Configuration
@RequiredArgsConstructor
public class DataLoader {

    private final RoomRepository roomRepository;

    @Bean
    CommandLineRunner loadData() {
        return args -> {

            if (roomRepository.count() == 0) {

                roomRepository.save(
                        new Room(null,"Single", 1, new ArrayList<>())
                );

                roomRepository.save(
                        new Room(null,"Double", 2, new ArrayList<>())
                );

                roomRepository.save(
                        new Room(null, "Single", 1, new ArrayList<>())
                );
            }
        };
    }
}
